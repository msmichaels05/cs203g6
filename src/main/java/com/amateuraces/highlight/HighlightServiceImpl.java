package com.amateuraces.highlight;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class HighlightServiceImpl implements HighlightService {
   
    private HighlightRepository highlights;
    public HighlightServiceImpl(HighlightRepository highlights){
        this.highlights = highlights;
    
    }

    @Override
    public List<Highlight> listHighlights() {
        return highlights.findAll();
    }
    
    @Override
    public Highlight getHighlight(Long year, Long month){
        Optional<Highlight> h = highlights.findCurrentHighlight(year, month);
        if (h.isPresent())
            return h.get();
        else
            return null;
    }
    
  
    @Override
    public Highlight addHighlight(Highlight highlight) {
        highlight.setYear(highlights.save(highlight));
        highlight.setMonth(highlights.save(highlight));
        return highlight;
    }

    @Override
    public Highlight updateHighlight(Long year, Long month, Highlight newHighlightInfo) {
   
        Optional<Highlight> existingHighlightOptional = highlights.findCurrentHighlight(year, month);
    
        Highlight highlight = newHighlightInfo;
        highlight.setYear(year);
        highlight.setMonth(month);
        return highlights.update(highlight)>0 ? highlight : null;
    }

    @Override
    public int deleteHighlight(Long year, Long month){
        return highlights.deleteByYearMonth(year, month);
    }
}