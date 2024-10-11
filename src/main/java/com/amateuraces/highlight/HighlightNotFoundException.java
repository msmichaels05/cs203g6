<<<<<<< HEAD:src/main/java/com/amateuraces/book/BookNotFoundException.java
package com.amateuraces.book;
=======
package com.amateuraces.highlight;
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68:src/main/java/com/amateuraces/highlight/HighlightNotFoundException.java

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
<<<<<<< HEAD:src/main/java/com/amateuraces/book/BookNotFoundException.java
public class BookNotFoundException extends RuntimeException{
=======
public class HighlightNotFoundException extends RuntimeException{
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68:src/main/java/com/amateuraces/highlight/HighlightNotFoundException.java
    /**
     *
     */
    private static final long serialVersionUID = 1L;

<<<<<<< HEAD:src/main/java/com/amateuraces/book/BookNotFoundException.java
    BookNotFoundException(Long id) {
        super("Could not find book " + id);
=======
    HighlightNotFoundException(Long id) {
        super("Could not find highlight " + id);
>>>>>>> a84a5a3c7149033df22ad0b28304fe880ce57d68:src/main/java/com/amateuraces/highlight/HighlightNotFoundException.java
    }
    
}
