package com.amateuraces.client;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.amateuraces.player.Player;

@Component
public class RestTemplateClient {
    
    private final RestTemplate template;

    /**
     * Add authentication information for the RestTemplate
     * 
     */
    public RestTemplateClient(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder
                .basicAuthentication("admin", "goodpassword")
                .build();
    }
    /**
     * Get a player with given id
     * 
     * @param URI
     * @param id
     * @return
     */
    public Player getPlayer(final String URI, final Long id) {
        final Player player = template.getForObject(URI + "/" + id, Player.class);
        return player;
    }

    // public Highlight getHighlight(final String URI, final Long id) {
    //     final Highlight highlight = template.getForObject(URI + "/" + id, Highlight.class);
    //     return highlight;
    // }

    /**
     * Add a new player
     * 
     * @param URI
     * @param newPlayer
     * @return
     */
    public Player addPlayer(final String URI, final Player newPlayer) {
        final Player returned = template.postForObject(URI, newPlayer, Player.class);
        
        return returned;
    }

    // public Highlight addHighlight(final String URI, final Highlight newHighlight) {
    //     final Highlight returned = template.postForObject(URI, newHighlight, Highlight.class);
        
    //     return returned;
    // }

    /**
     * Get a player, but return a HTTP response entity.
     * @param URI
     * @param id
     * @return
     */
    public ResponseEntity<Player> getPlayerEntity(final String URI, final Long id){
        return template.getForEntity(URI + "/{id}", Player.class, Long.toString(id));
    }

    // public ResponseEntity<Highlight> getHighlighEntity(final String URI, final Long id){
    //     return template.getForEntity(URI + "/{id}", Highlight.class, Long.toString(id));
    // }
    
}