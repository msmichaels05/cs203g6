package com.amateuraces.highlight;

import java.util.List;

public interface HighlightService {
    List<Highlight> listHighlights();
    Highlight getHighlight(HighlightKey id);
    Highlight addHighlight(Highlight highlight);
    Highlight updateHighlight(HighlightKey id, Highlight highlight);
    void deleteHighlight(HighlightKey id);
}