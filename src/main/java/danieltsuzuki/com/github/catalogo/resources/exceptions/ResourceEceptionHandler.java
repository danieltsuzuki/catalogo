package danieltsuzuki.com.github.catalogo.resources.exceptions;

import danieltsuzuki.com.github.catalogo.services.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceEceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> EntityNotFound(EntityNotFoundException e, HttpServletRequest request){
        StandardError err = new StandardError(
                Instant.now(), HttpStatus.NOT_FOUND.value(),
                "Resource not found", e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

}
