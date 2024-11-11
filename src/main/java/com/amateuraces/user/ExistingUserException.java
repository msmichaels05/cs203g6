
package com.amateuraces.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class ExistingUserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExistingUserException(String message) {
        super(message);
    }

}
