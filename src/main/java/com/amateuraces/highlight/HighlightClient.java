package com.amateuraces.highlight;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HighlightClient {
    
    private final RestTemplate template;
    
    HighlightClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }
    
    /**
     * 
     * @param URI
     * @param year
     * @param month
     * @return
     */
    public Highlight getHighlight(final String URI, final Highlight id) {
        final Highlight highlight = template.getForObject(URI + "/" + id, Highlight.class);
        return highlight;
    }

    /**
     * Add a new highlight
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
