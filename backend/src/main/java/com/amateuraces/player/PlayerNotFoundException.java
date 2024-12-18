package com.amateuraces.player;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class PlayerNotFoundException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public PlayerNotFoundException(Long id) {
        super("Could not find player " + id);
    }

    public PlayerNotFoundException(String message) {
        super(message);
    }

    public PlayerNotFoundException(Long id1, Long id2) {
        super("Could not find either player ID " + id1 + " or player ID" + id2);
    }
}