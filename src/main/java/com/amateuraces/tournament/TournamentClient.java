package com.amateuraces.tournament;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TournamentClient {

    private final RestTemplate template;

    TournamentClient(RestTemplateBuilder builder) {
        this.template = builder.build();
    }

    public Tournament getTournament(final String URI, final Long id) {
        final Tournament tournament = template.getForObject(URI + "/" + id, Tournament.class);
        return tournament;
    }

    public Tournament addTournament(final String URI, final Tournament newTournament) {
        final Tournament returned = template.postForObject(URI, newTournament, Tournament.class);
        return returned;
    }
}
