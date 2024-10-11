package com.amateuraces.match;

<<<<<<< HEAD
import com.amateuraces.player.Player;
import com.amateuraces.match.Match;
import com.amateuraces.tournament.tournament;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository {

    Match save(Match match);

    Match findById(Long matchId);

    List<Match> findAll();

    Match update(Match match);

    void delete(Long matchId);

    List<Match> findByTournamentId(Long tournamentId);

    void recordMatchResult(Long matchId, Long winnerId); // New method for recording results

    void rescheduleMatch(Long matchId, LocalDateTime newDateTime); // New method for rescheduling matches

    void cancelMatch(Long matchId); // New method for cancelling matches
}
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * We only need this interface declaration
 * Spring will automatically generate an implementation of the repo
 * 
 * JpaRepository provides more features by extending PagingAndSortingRepository, which in turn extends CrudRepository
 * For the purpose of this exercise, CrudRepository would also be sufficient
 */
@Repository
public interface MatchRepository extends JpaRepository <Match, Long>{
}
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
