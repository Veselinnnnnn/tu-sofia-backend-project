package com.universityproject.backendproject.exception;

import com.universityproject.backendproject.exception.event.EventCreationException;
import com.universityproject.backendproject.exception.event.EventNotFoundException;
import com.universityproject.backendproject.exception.event.EventUpdateException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + ex.getMessage());
    }

    @ExceptionHandler({
            ApplicationDetailsNotFoundException.class,
            ResourceNotFoundException.class,
            AnimalNotFoundException.class,
            AdminUserNotFoundException.class,
            EventNotFoundException.class,
            TestimonialNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(Exception e) {
        return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Not Found", "/resource");
    }

    @ExceptionHandler({
            EmailSendException.class,
            InvalidPasswordException.class,
            RoleNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception e) {
        return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Bad Request", "/auth/register");
    }

    @ExceptionHandler({
            EventCreationException.class,
            EventUpdateException.class,
            DuplicateUserException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictExceptions(Exception e) {
        return createErrorResponse(e.getMessage(), HttpStatus.CONFLICT, "Conflict", "/auth/register");
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status, String error, String path) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                path
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
