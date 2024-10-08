package com.amateuraces.highlight;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class HighlightController {
    private final HighlightService highlightService;

    public HighlightController(HighlightService hs){
        highlightService = hs;
    }

    @GetMapping("/highlights")
    public List<Highlight> getHighlights(){
        return highlightService.listHighlights();
    }

    @GetMapping("/highlights/{year}/{month}")
    public Highlight getHighlight(@PathVariable Long year, @PathVariable Long month){
        HighlightKey id = new HighlightKey(year, month);
        Highlight highlight = highlightService.getHighlight(id);

        if(highlight == null) throw new HighlightNotFoundException(id);
        return highlightService.getHighlight(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/highlights")
    public Highlight addHighlight(@RequestBody Highlight highlight){
        return highlightService.addHighlight(highlight);
    }

    /**
     * If there is no book with the given "id", throw a BookNotFoundException
     * @param id
     * @param newBookInfo
     * @return the updated, or newly added book
     */
    @PutMapping("/highlights/{year}/{month}")
    public Highlight updateHighlight(@PathVariable Long year, @PathVariable Long month, @Valid @RequestBody Highlight newHighlightInfo){
        HighlightKey id = new HighlightKey(year, month);
        Highlight highlight = highlightService.updateHighlight(id, newHighlightInfo);
        if(highlight == null) throw new HighlightNotFoundException(id);
        return highlight;
    }

    /**
     * Remove a book with the DELETE request to "/books/{id}"
     * If there is no book with the given "id", throw a BookNotFoundException
     * @param id
     */
    @DeleteMapping("/highlights/{year}/{month}")
    public void deleteHighlight(@PathVariable Long year, @PathVariable Long month){
        HighlightKey id = new HighlightKey(year, month);
        try{
            highlightService.deleteHighlight(id);
         }catch(EmptyResultDataAccessException e) {
            throw new HighlightNotFoundException(id);
         }
    }
}
