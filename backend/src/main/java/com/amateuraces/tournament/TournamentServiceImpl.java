package com.amateuraces.tournament;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.amateuraces.match.Match;
import com.amateuraces.match.MatchRepository;
import com.amateuraces.player.Player;
import com.amateuraces.player.PlayerNotFoundException;
import com.amateuraces.player.PlayerRepository;
import com.amateuraces.user.User;
import com.amateuraces.user.UserAuthenticationException;

import jakarta.transaction.Transactional;

@Service
public class TournamentServiceImpl implements TournamentService {

    private  TournamentRepository tournamentRepository;
    private PlayerRepository playerRepository;
    private MatchRepository matchRepository;
    // private CustomUserDetailsService userDetailsService;

    public TournamentServiceImpl(TournamentRepository tournamentRepository, PlayerRepository playerRepository,
    MatchRepository matchRepository) {
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        // this.userDetailsService = userDetailsService;
    }


    @Override
    public List<Tournament> listTournaments() {
        // This uses the JPA method findAll() provided by JpaRepository
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament getTournament(Long id){
        return tournamentRepository.findById(id).orElse(null);
    }

    @Override
    public Tournament addTournament(Tournament tournament) {
        if (tournament.getName()==null || tournament.getName().isEmpty()){
            throw new TournamentNotFoundException(tournament.getId());
        }

        // Check if a tournament with the same name already exists
        if (tournamentRepository.findByName(tournament.getName()).isPresent()) {
            throw new ExistingTournamentException("Tournament with this name already exists");
        }

        LocalDate currentDate = LocalDate.now();

        // Check if the tournament's start date is at least one month in the future
        if (!tournament.getStartDate().isAfter(currentDate.plusMonths(1))) {
            throw new IllegalArgumentException("The tournament must be scheduled at least one month in advance from today.");
        }
    
        // Validate that the start date is before the end date
        if (!tournament.getStartDate().isBefore(tournament.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        // Set Registration period : 1 week before start date
        LocalDate registrationEndDate = tournament.getStartDate().minusWeeks(1);
        tournament.setRegistrationEndDate(registrationEndDate);
        return tournamentRepository.save(tournament);
    }

        /**
     * Remove a Tournament with the given id
     * Spring Data JPA does not return a value for delete operation
     * Cascading: removing a player will also remove all its associated reviews
     */
    @Override
    public void deleteTournament(Long id){
    // Check if the tournament exists before attempting to delete
    if (!tournamentRepository.existsById(id)) {
        throw new TournamentNotFoundException(id);
    }
    
    // If the tournament exists, delete them
    tournamentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void joinTournament(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            Long userId = userDetails.getId(); // This is also the player ID

            // Now you can find the player by userId
            Player player = playerRepository.findById(userId)
                    .orElseThrow(() -> new PlayerNotFoundException("Player not registered"));
    
            // Continue with your logic to join the tournament...
            Tournament tournament = tournamentRepository.findById(id)
                    .orElseThrow(() -> new TournamentNotFoundException(id));
            
            //Check gender of the tournament
            if (!(tournament.getGender().equals(player.getGender()))){
                throw new IllegalArgumentException("Only "+ tournament.getGender()+" allowed");
            }

            //Check Elo requirement to join tournament
            if(tournament.getELORequirement() > player.getElo()){
                throw new IllegalArgumentException("Min elo requirement: "+ tournament.getELORequirement());
            }

            //Check if the player is already in the tournament
            if(tournament.getPlayers().contains(player)){
                throw new IllegalArgumentException("Player has already joined this tournament.");
            }
            
            //Check if tournament is full
            if (tournament.getPlayerCount() >= tournament.getMaxPlayers()) {
                throw new IllegalArgumentException("Cannot join tournament: it is full.");
            }

            tournament.getPlayers().add(player);
            //Increment player count
            tournament.setPlayerCount(tournament.getPlayerCount()+1);
            tournamentRepository.save(tournament);
        } else {
            throw new UserAuthenticationException();
        }
    }

    @Override
    public Set<Player> getPlayersInTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        return tournament.getPlayers();
    }


    @Override
    public Tournament updateTournament(Long id, Tournament newTournamentInfo) {
        return tournamentRepository.findById(id).map(tournament -> {
            tournament.setName(newTournamentInfo.getName());
            tournament.setELORequirement(newTournamentInfo.getELORequirement());
            tournament.setLocation(newTournamentInfo.getLocation());
            tournament.setMaxPlayers(newTournamentInfo.getMaxPlayers());
            tournament.setDescription(newTournamentInfo.getDescription());
            tournament.setStartDate(newTournamentInfo.getStartDate());
            tournament.setEndDate(newTournamentInfo.getEndDate());
            tournament.setGender(newTournamentInfo.getGender());
            return tournamentRepository.save(tournament);
        }).orElse(null);
    }


    @Override
    public void removePlayerFromTournament(Long tournamentId, Long playerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User userDetails = (User) authentication.getPrincipal();
            Long userId = userDetails.getId(); // This is also the player ID

            // Now you can find the player by userId
            Player player = playerRepository.findById(userId)
                    .orElseThrow(() -> new PlayerNotFoundException("Player not registered"));

            Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
    
            tournament.getPlayers().remove(player);
            tournament.setPlayerCount(tournament.getPlayerCount()-1);
            tournamentRepository.save(tournament);
        } else {
            throw new UserAuthenticationException();
        }
    }
    /**
     * Creates matches for the given tournament by pairing up players.
     * 
     * @param tournamentId the ID of the tournament to create matches for.
     * @return a list of created matches.
     */

    // @Transactional
    // public List<Match> createMatchesForTournament(Long tournamentId) {
    //     Tournament tournament = tournamentRepository.findById(tournamentId)
    //             .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

    //     Set<Player> players = tournament.getPlayers(); // Get the players in the tournament

    //     // Generate matches by pairing players
    //     List<Match> matches = new ArrayList<>();
    //     Player[] playerArray = players.toArray(new Player[0]); // Convert the set to an array for pairing

    //     for (int i = 0; i < playerArray.length; i += 2) {
    //         if (i + 1 < playerArray.length) {
    //             Player player1 = playerArray[i];
    //             Player player2 = playerArray[i + 1];

    //             // Create a match and set tournament and players
    //             Match match = new Match(tournament,player1,player2);
    //             // Save the match to the database
    //             matchRepository.save(match);
    //             matches.add(match);
    //         } else {
    //             // If there’s an odd number of players, one player gets a bye (no opponent)
    //             Player player1 = playerArray[i];
    //             Match match = new Match(this);
    //             match.setTournament(tournament);
    //             match.setPlayer1(player1);
    //             match.setPlayer2(null); // No opponent for the player

    //             // Save the match to the database
    //             matchRepository.save(match);
    //             matches.add(match);
    //         }
    //     }

    //     return matches; // Return the list of matches that were created
    // }

    @Override
    public List<Match> initialiseDraw(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with id " + tournamentId));

        List<Match> matches = tournament.initialiseDraw();
        tournament.setMatches(matches);
        tournamentRepository.save(tournament); // Save the updated tournament with the matches
        return tournament.getMatches();
    }

    @Override
    public List<Match> updateNextRound(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with id " + tournamentId));

        List<Match> updatedMatches = tournament.updateNextRound();

        // Save the updated tournament with the new matches
        tournamentRepository.save(tournament);

        return updatedMatches;
    }

    // @Transactional
    // public List<Match> generateMatchesByTournamentId(Long tournamentId) {
    //     Tournament tournament = tournamentRepository.findById(tournamentId)
    //             .orElseThrow(() -> new TournamentNotFoundException(tournamentId));

    //     int totalPlayers = tournament.getPlayerCount();
    //     Set<Player> registeredPlayers = tournament.getPlayers();

    //     // Determine nearest power of two and excess players
    //     int nearestPowerOfTwo = calculateNearestPowerOfTwo(totalPlayers);
    //     int excessPlayers = totalPlayers - nearestPowerOfTwo;
    //     int treeHeight = (int) Math.floor(Math.log(totalPlayers) / Math.log(2)) - 1;
    //     int nonPreliminaryPlayers = nearestPowerOfTwo - excessPlayers;

    //     // Initialize the match array for the tournament structure (similar to a heap)
    //     Match[] matches = initializeMatchesArray(tournament, treeHeight);

    //     // Populate initial matches based on number of players and round structure
    //     if (totalPlayers == nearestPowerOfTwo) {
    //         populateMatchesForExactPowerOfTwo(matches, registeredPlayers, nearestPowerOfTwo);
    //     } else {
    //         populateMatchesForNonPowerOfTwo(matches, registeredPlayers, nonPreliminaryPlayers, excessPlayers, treeHeight);
    //     }

    //     // Save and return generated matches
    //     return saveMatches(matches);
    // }

    private int calculateNearestPowerOfTwo(int totalPlayers) {
        // Find the largest power of two less than or equal to totalPlayers
        return (int) Math.pow(2, Math.floor(Math.log(totalPlayers) / Math.log(2)));
    }

    private Match[] initializeMatchesArray(Tournament tournament, int treeHeight) {
        // Initialize a match array for the tournament structure as a heap
        int totalMatches = (int) Math.pow(2, treeHeight + 1) - 1;
        Match[] matches = new Match[totalMatches + 1]; // Extra match for bronze medal match

        // Create empty matches in the match tree structure
        for (int i = 0; i < treeHeight; i++) {
            int numberOfMatchesAtHeight = (int) Math.pow(2, i);
            int firstMatchAtHeight = numberOfMatchesAtHeight - 1;

            for (int j = 0; j < numberOfMatchesAtHeight; j++) {
                int matchIndex = firstMatchAtHeight + j;
                // MatchId matchId = new MatchId(tournament.getTournamentId(), (long) matchIndex);
                matches[matchIndex] = new Match(tournament, null, null);
            }
        }

        // Create an extra match for bronze medal match
        // MatchId matchId = new MatchId(tournament.getTournamentId(), (long) totalMatches);
        // matches[totalMatches] = new Match(matchId, tournament, null, null, "Bronze Medal Match", null);

        return matches;
    }

    // private void populateMatchesForExactPowerOfTwo(Match[] matches, Set<Player> players, int nearestPowerOfTwo) {
    //     // Populate matches for when totalPlayers is exactly a power of two
    //     int matchIndex = nearestPowerOfTwo;
    //     for (int i = 0; i < players.size(); i++) {
    //         if (i % 2 == 0) {
    //             matches[matchIndex + i / 2].setPlayer1(players.get(i).getPlayer());
    //         } else {
    //             matches[matchIndex + i / 2].setPlayer2(players.get(i).getPlayer());
    //         }
    //     }
    // }

    // private void populateMatchesForNonPowerOfTwo(Match[] matches, Set<Player> players, int nonPreliminaryPlayers, int excessPlayers, int treeHeight) {
    //     // Handle non-preliminary players
    //     int matchIndexAfterPreliminaries = (int) Math.pow(2, treeHeight - 1);
    //     for (int i = 0; i < nonPreliminaryPlayers; i++) {
    //         if (i % 2 == 0) {
    //             matches[matchIndexAfterPreliminaries + i / 2].setPlayer1(players.get(i).getPlayer());
    //         } else {
    //             matches[matchIndexAfterPreliminaries + i / 2].setPlayer2(players.get(i).getPlayer());
    //         }
    //     }

    //     // Handle excess players for preliminary matches
    //     int preliminaryStartIndex = nonPreliminaryPlayers % 2 == 0 ?
    //             2 * (matchIndexAfterPreliminaries + nonPreliminaryPlayers / 2) + 1 :
    //             2 * (matchIndexAfterPreliminaries + nonPreliminaryPlayers / 2) + 2;

    //     for (int i = 0; i < 2 * excessPlayers; i++) {
    //         if (i % 2 == 0) {
    //             matches[preliminaryStartIndex + i / 2].setPlayer1(players.get(nonPreliminaryPlayers + i).getPlayer());
    //         } else {
    //             matches[preliminaryStartIndex + i / 2].setPlayer2(players.get(nonPreliminaryPlayers + i).getPlayer());
    //         }
    //     }
    // }

    // private List<Match> saveMatches(Match[] matches) {
    //     // Save all matches to the repository and return the saved matches
    //     List<Match> matchList = Arrays.asList(matches);
    //     for (Match match : matchList) {
    //         matchRepository.save(match);
    //     }
    //     return matchList;
    // }

    // Calculate the elo gain for each player based on the match result
    public static int calculateEloGain(Player player1, Player player2, String set1Score, String set2Score, String set3Score) {
        int eloGain = 0;
        int eloDifference = (int) (player1.getElo() - player2.getElo());
        int expectedScore = 1 / (1 + (int) Math.pow(10, eloDifference / 400.0));

        int actualScore = calculateActualScore(set1Score, set2Score, set3Score);
        eloGain = (int) Math.round(32 * (actualScore - expectedScore));
        return eloGain;
    }

    // Method to get performance point if the score in a match dominates
    public static int getPerformancePoint(String set1Score, String set2Score, String set3Score) {
        int performancePoint = calculateTotalScore(set1Score, set2Score, set3Score);
        return performancePoint;
    }

    // Helper method to calculate the actual score based on the match result
    private static int calculateActualScore(String set1Score, String set2Score, String set3Score) {
        int set1Score1 = Integer.parseInt(set1Score.split("-")[0]);
        int set1Score2 = Integer.parseInt(set1Score.split("-")[1]);
        int set2Score1 = Integer.parseInt(set2Score.split("-")[0]);
        int set2Score2 = Integer.parseInt(set2Score.split("-")[1]);
        int totalScore1 = set1Score1 + set2Score1;
        int totalScore2 = set1Score2 + set2Score2;

        if (set3Score != null) {
            int set3Score1 = Integer.parseInt(set3Score.split("-")[0]);
            int set3Score2 = Integer.parseInt(set3Score.split("-")[1]);
            totalScore1 += set3Score1;
            totalScore2 += set3Score2;
        }

        return totalScore1 > totalScore2 ? 1 : 0;
    }

    // Helper method to calculate the total score difference
    private static int calculateTotalScore(String set1Score, String set2Score, String set3Score) {
        int set1Score1 = Integer.parseInt(set1Score.split("-")[0]);
        int set1Score2 = Integer.parseInt(set1Score.split("-")[1]);
        int set2Score1 = Integer.parseInt(set2Score.split("-")[0]);
        int set2Score2 = Integer.parseInt(set2Score.split("-")[1]);
        int totalScore1 = set1Score1 + set2Score1;
        int totalScore2 = set1Score2 + set2Score2;

        if (set3Score != null) {
            int set3Score1 = Integer.parseInt(set3Score.split("-")[0]);
            int set3Score2 = Integer.parseInt(set3Score.split("-")[1]);
            totalScore1 += set3Score1;
            totalScore2 += set3Score2;
        }

        return totalScore1 - totalScore2;
    }
}