package com.amateuraces.highlight;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amateuraces.player.PlayerRepository;
import com.amateuraces.tournament.TournamentRepository;

@Service
public class HighlightServiceImpl implements HighlightService {

    private HighlightRepository highlights;
    private TournamentRepository tournaments;
    private PlayerRepository players;

    public HighlightServiceImpl(HighlightRepository highlights, TournamentRepository tournaments,
            PlayerRepository players) {
        this.highlights = highlights;
        this.tournaments = tournaments;
        this.players = players;
    }

    @Override
    public List<Highlight> listHighlights() {
        return highlights.findAll();
    }

    @Override
    public Highlight getHighlight(Long id) {
        return highlights.findById(id).orElse(null);
    }

    @Override
    public Highlight addHighlight(Highlight highlight) {
        return highlights.save(highlight);
    }

    @Override
    public Highlight updateHighlight(Long id, Highlight newHighlightInfo) {
        return highlights.findById(id).map(highlight -> {
            highlight.setTournamentOfTheMonth(newHighlightInfo.getTournamentOfTheMonth());

            return highlights.save(highlight);
        }).orElse(null);
    }

    @Override
    public void deleteHighlight(Long id) {
        if (!highlights.existsById(id)) {
            throw new HighlightNotFoundException(id);
        }

        // If the highlight exists, delete them
        highlights.deleteById(id);
    }
}