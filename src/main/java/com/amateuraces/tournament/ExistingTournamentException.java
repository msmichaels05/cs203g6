package com.amateuraces.tournament;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class ExistingTournamentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExistingTournamentException(String message) {
        super(message);
    }

}