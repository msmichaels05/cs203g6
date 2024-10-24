package com.amateuraces.highlight;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class HighlightNotFoundException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    HighlightNotFoundException(Long id) {
        super("Could not find highlight " + id);
    }
    
}
