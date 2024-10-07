package com.amateuraces.highlight;

import com.amateuraces.book.Book;
import com.amateuraces.highlights.*;

public interface HighlightService {
    Highlight calculateHighlight();
    Highlight getCurrentHighlight();
    List<Highlights> listHighlight();
    Highlight getHighlight(int month, int year);
    Highlight addHighlight(Highlight highlight);
}