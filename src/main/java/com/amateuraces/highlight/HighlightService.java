package com.amateuraces.highlight;

import java.util.List;

public interface HighlightService {
    List<Highlight> listHighlights();
    Highlight getHighlight(Long id);
    Highlight addHighlight(Highlight highlight);
    Highlight updateHighlight(Long id, Highlight highlight);
    void deleteHighlight(Long id);
}