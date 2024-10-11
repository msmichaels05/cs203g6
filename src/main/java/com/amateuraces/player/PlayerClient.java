package com.amateuraces.player;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PlayerClient {

<<<<<<< HEAD
    private RestTemplate template;
=======
    private final RestTemplate template;
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68

    PlayerClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }

<<<<<<< HEAD
    /**

    Get a
    Player with
    given id**

    @param URI
     * @param id
     * @return
     */
=======
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
    public Player getPlayer(final String URI, final Long id) {
        final Player player = template.getForObject(URI + "/" + id, Player.class);
        return player;
    }

<<<<<<< HEAD
    /**

    Add a new Player**

    @param URI
     * @param newPlayer
     * @return
     */
=======
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68
    public Player addPlayer(final String URI, final Player newPlayer) {
        final Player returned = template.postForObject(URI, newPlayer, Player.class);
        return returned;
    }
}