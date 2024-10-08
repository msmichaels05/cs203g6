package com.amateuraces.player;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PlayerClient {
    
    private RestTemplate template;
    
    PlayerClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }
    
    /**
     * Get a Player with given id
     * 
     * @param URI
     * @param id
     * @return
     */
    public Player getPlayer(final String URI, final Long id) {
        final Player player = template.getForObject(URI + "/" + id, Player.class);
        return player;
    }

    /**

    Add a new Player**

    @param URI
     * @param newPlayer
     * @return
     */
    public Player addPlayer(final String URI, final Player newPlayer) {
        final Player returned = template.postForObject(URI, newPlayer, Player.class);
        return returned;
    }
}