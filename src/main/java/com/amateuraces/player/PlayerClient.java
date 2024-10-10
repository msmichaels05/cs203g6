package com.amateuraces.player;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PlayerClient {

    private final RestTemplate template;

    PlayerClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }

    public Player getPlayer(final String URI, final Long id) {
        final Player player = template.getForObject(URI + "/" + id, Player.class);
        return player;
    }

    public Player addPlayer(final String URI, final Player newPlayer) {
        final Player returned = template.postForObject(URI, newPlayer, Player.class);
        return returned;
    }
}