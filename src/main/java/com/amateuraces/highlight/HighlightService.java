package com.amateuraces.highlight;

import java.util.List;

public interface HighlightService {
    List<Highlight> listHighlights();
    Highlight getHighlight(Long year, Long month);
    Highlight addHighlight(Highlight highlight);
    Highlight updateHighlight(Long year, Long month, Highlight highlight);
    int deleteHighlight(Long year, Long month);

}