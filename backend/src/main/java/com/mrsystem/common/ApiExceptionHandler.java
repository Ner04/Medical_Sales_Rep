package com.mrsystem.common;

import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(BadCredentialsException.class)
  ResponseEntity<Map<String, Object>> badCredentials(BadCredentialsException ex) {
    return error(HttpStatus.UNAUTHORIZED, "Invalid username or password");
  }

  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<Map<String, Object>> denied(AccessDeniedException ex) {
    return error(HttpStatus.FORBIDDEN, "You do not have permission for this action");
  }

  @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
  ResponseEntity<Map<String, Object>> badRequest(Exception ex) {
    return error(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Map<String, Object>> invalid(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(error -> error.getField() + " " + error.getDefaultMessage())
        .orElse("Validation failed");
    return error(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<Map<String, Object>> fallback(Exception ex) {
    return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");
  }

  private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(Map.of(
        "timestamp", Instant.now().toString(),
        "status", status.value(),
        "error", status.getReasonPhrase(),
        "message", message));
  }
}
