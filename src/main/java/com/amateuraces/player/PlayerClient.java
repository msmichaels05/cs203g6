package com.amateuraces.player;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PlayerClient {
<<<<<<< HEAD
    
    private RestTemplate template;
    
    PlayerClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }
    
    /**
     * Get a Player with given id
     * 
     * @param URI
=======

    private RestTemplate template;

    PlayerClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }

    /**

    Get a
    Player with
    given id**

    @param URI
>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347
     * @param id
     * @return
     */
    public Player getPlayer(final String URI, final Long id) {
        final Player player = template.getForObject(URI + "/" + id, Player.class);
        return player;
    }

    /**
<<<<<<< HEAD
     * Add a new Player
     * 
     * @param URI
=======

    Add a new Player**

    @param URI
>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347
     * @param newPlayer
     * @return
     */
    public Player addPlayer(final String URI, final Player newPlayer) {
<<<<<<< HEAD
        final player returned = template.postForObject(URI, newPlayer, Player.class);
=======
        final Player returned = template.postForObject(URI, newPlayer, Player.class);
>>>>>>> 3e3de983188d74e9d50551f17476ca4dfd004347
        return returned;
    }
}