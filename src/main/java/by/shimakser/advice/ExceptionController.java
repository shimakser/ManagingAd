package by.shimakser.advice;

import by.shimakser.exception.ExceptionMessage;
import javassist.NotFoundException;
import netscape.security.ForbiddenTargetException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.rmi.AlreadyBoundException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleNotFoundException(NotFoundException e) {
        ExceptionMessage response = new ExceptionMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyBoundException.class)
    public ResponseEntity<ExceptionMessage> handleAlreadyIsTakenException(AlreadyBoundException e) {
        ExceptionMessage response = new ExceptionMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenTargetException.class)
    public ResponseEntity<ExceptionMessage> handleForbiddenException(ForbiddenTargetException e) {
        ExceptionMessage response = new ExceptionMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
