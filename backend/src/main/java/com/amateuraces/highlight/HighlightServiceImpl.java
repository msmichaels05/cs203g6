package com.amateuraces.highlight;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amateuraces.player.PlayerRepository;
import com.amateuraces.tournament.TournamentRepository;

@Service
public class HighlightServiceImpl implements HighlightService {

    private HighlightRepository highlightRepository;
    private TournamentRepository tournamentRepository;
    private PlayerRepository playerRepository;

    public HighlightServiceImpl(HighlightRepository highlightRepository, TournamentRepository tournamentRepository,
            PlayerRepository playerRepository) {
        this.highlightRepository = highlightRepository;
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Highlight> listHighlights() {
        return highlightRepository.findAll();
    }

    @Override
    public Highlight getHighlight(Long id) {
        return highlightRepository.findById(id).orElse(null);
    }

    @Override
    public Highlight addHighlight(Highlight highlight) {
        return highlightRepository.save(highlight);
    }

    @Override
    public Highlight updateHighlight(Long id, Highlight newHighlightInfo) {
        return highlightRepository.findById(id).map(highlight -> {
            highlight.setTournamentOfTheMonth(newHighlightInfo.getTournamentOfTheMonth());

            return highlightRepository.save(highlight);
        }).orElse(null);
    }

    @Override
    public void deleteHighlight(Long id) {
        if (!highlightRepository.existsById(id)) {
            throw new HighlightNotFoundException(id);
        }

        // If the highlight exists, delete them
        highlightRepository.deleteById(id);
    }
}