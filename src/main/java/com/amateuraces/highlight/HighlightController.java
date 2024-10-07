package com.amateuraces.highlight;

import javax.swing.text.Highlighter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.amateuraces.highlight.HighlightsService;

@RestController
public class HighlightController {
    private HighlightService highlightService;

    public HighlightsController(HighlightService hs){
        highlightService = hs;
    }

    @GetMapping("/highlight")
    public List<Highlight> getHighlight(){
        return highlightsService.listHighlight();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/highlight")
    public Highlight addHighlight(@RequestBody Highlight highlight){
        return highlightService.addHighlight(highlight);
    }
}
