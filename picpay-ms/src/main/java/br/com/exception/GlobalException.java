package br.com.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(NullValueException.class)
    public ResponseEntity<Map<String, Object>> handlerNullValueException(NullValueException exception, HttpServletRequest http){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fieldError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exception.getMessage(),
                http.getServletPath()
        ));
    }

    @ExceptionHandler(AccountNumberNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlerAccountNumberNotFoundException(AccountNumberNotFoundException exception, HttpServletRequest http){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fieldError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                http.getServletPath()
        ));
    }

    @ExceptionHandler(ValueTransactionException.class)
    public ResponseEntity<Map<String, Object>> handlerValueTransactionException(ValueTransactionException exception, HttpServletRequest http){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fieldError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exception.getMessage(),
                http.getServletPath()
        ));
    }

    public Map<String, Object> fieldError(int status, String error, String message, String path){
        Map<String, Object> field = new HashMap<>();
        field.put("timestamp", LocalDateTime.now());
        field.put("status", status);
        field.put("error", error);
        field.put("message", message);
        field.put("path", path);

        log.error(field.toString());

        return field;
    }
}
