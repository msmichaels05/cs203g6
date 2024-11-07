package com.amateuraces.match;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class PlayerNotPartOfMatchException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public PlayerNotPartOfMatchException(Long playerId, Long matchId) {
        super("Player with ID " + playerId + " is not part of match with ID " + matchId);
    }
    
    public PlayerNotPartOfMatchException(Long playerId1, Long playerId2, Long matchId) {
        super("Either player with ID " + playerId1 + " or ID " + playerId2 + " is not part of match with ID " + matchId);
    }
}