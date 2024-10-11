package com.amateuraces.match;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MatchClient {
    private final RestTemplate template;

    MatchClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }

    public Match getMatch(final String URI, final Long id) {
        final Match player = template.getForObject(URI + "/" + id, Match.class);
        return player;
    }

    public Match addMatch(final String URI, final Match newMatch) {
        final Match returned = template.postForObject(URI, newMatch, Match.class);
        return returned;
    }
}
