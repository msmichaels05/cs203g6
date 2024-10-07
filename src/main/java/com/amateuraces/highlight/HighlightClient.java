package com.amateuraces.highlight;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HighlightClient {
    
    private RestTemplate template;
    
    HighlightClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }
    
    /**
     * Get a book with given id
     * 
     * @param URI
     * @param year
     * @param month
     * @return
     */
    public Highlight getHighlight(final String URI, final int year, final int month) {
        final Highlight highlight = template.getForObject(URI + "/" + year + "/" + month, Highlight.class);
        return highlight;
    }

    /**
     * Add a new book
     * 
     * @param URI
     * @param newHighlight
     * @return
     */
    public Highlight addHighlight(final String URI, final Highlight newHighlight) {
        final Highlight returned = template.postForObject(URI, newHighlight, Highlight.class);
        return returned;
    }
}
