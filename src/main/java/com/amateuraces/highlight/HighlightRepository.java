package com.amateuraces.highlight;

import java.util.List;
import java.util.Optional;

public interface HighlightRepository {
    Long save(Highlight highlight);
    int update(Highlight highlight);
    int deleteByYearMonth(Long year, Long month);
    List<Highlight> findAll();
    Optional<Highlight> findCurrentHighlight(Long year, Long month);
}