package com.amateuraces.highlight;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HighlightController {
    private HighlightService highlightService;

    public HighlightController(HighlightService hs){
        highlightService = hs;
    }

    @GetMapping("/highlight")
    public List<Highlight> getHighlight(){
        return highlightService.listHighlights();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/highlight")
    public Highlight addHighlight(@RequestBody Highlight highlight){
        return highlightService.addHighlight(highlight);
    }
}
