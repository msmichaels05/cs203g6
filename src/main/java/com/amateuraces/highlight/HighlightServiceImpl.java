package com.amateuraces.highlight;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class HighlightServiceImpl implements HighlightService {
   
    private final HighlightRepository highlights;

    public HighlightServiceImpl(HighlightRepository highlights){
        this.highlights = highlights;
    }

    @Override
    public List<Highlight> listHighlights() {
        return highlights.findAll();
    }
    
    @Override
    public Highlight getHighlight(HighlightKey id){
        return highlights.findById(id).orElse(null);
    }
    
  
    @Override
    public Highlight addHighlight(Highlight highlight) {
        return highlights.save(highlight);
    }

    @Override
    public Highlight updateHighlight(HighlightKey id, Highlight newHighlightInfo) {
        return highlights.findById(id).map(highlight -> {highlight.setTournamentOfTheMonth(newHighlightInfo.getTournamentOfTheMonth());
            return highlights.save(highlight);
        }).orElse(null);
    }

    @Override
    public void deleteHighlight(HighlightKey id){
        if (!highlights.existsById(id)) {
            throw new HighlightNotFoundException(id);
        }
    
        // If the highlight exists, delete them
        highlights.deleteById(id);
    }
}