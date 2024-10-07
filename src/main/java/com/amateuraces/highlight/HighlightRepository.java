package com.amateuraces.highlight;

public interface HighlightRepository {
    Long save(Highlight highlight);
    int update(Highlight highlight);
    int deleteByYearMonth(int year, int month);
    List<Highlight> findAll();
    Optional<Highlight> findCurrentHighlight();
}