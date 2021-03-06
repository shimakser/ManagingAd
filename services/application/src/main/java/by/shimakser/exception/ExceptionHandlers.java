package by.shimakser.exception;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleNotFoundException(NotFoundException e) {
        ExceptionMessage response = new ExceptionMessage(e.getMessage());
        log.error("NotFoundException", e);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleFileNotFoundException(FileNotFoundException e) {
        ExceptionMessage response = new ExceptionMessage(e.getMessage());
        log.error("FileNotFoundException", e);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleEntityNotFoundException(EntityNotFoundException e) {
        ExceptionMessage response = new ExceptionMessage(e.getMessage());
        log.error("EntityNotFoundException", e);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionMessage> handleAlreadyIsTakenException(EntityExistsException e) {
        ExceptionMessage response = new ExceptionMessage(e.getMessage());
        log.error("EntityExistsException", e);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionMessage> handleAuthenticationException(AuthenticationException e) {
        ExceptionMessage response = new ExceptionMessage(e.getMessage());
        log.error("AuthenticationException", e);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthorizationServiceException.class)
    public ResponseEntity<ExceptionMessage> handleAuthorizationException(AuthorizationServiceException e) {
        ExceptionMessage response = new ExceptionMessage(e.getMessage());
        log.error("AuthorizationServiceException", e);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
