package com.SIMCHAT_A.SIMCHAT_A.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Map<String, Object> /* body = new HashMap */ buildErrorResponse(HttpStatus status, String message) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        /*
         * == > hell nah json {"time" : "2026 -9- 7T04:44:03.123456"
         * "status" : 400,
         * "error" : "Bad Request",
         * "message" : "Invalid input"}
         */
        return body;

    }

    // => 404
    @ExceptionHandler(ChatRoomNotFound.class)
    public ResponseEntity<Map<String, Object>> HandlerChatRoomNotFound(ChatRoomNotFound ex) {
        return new ResponseEntity<>(buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()),
                HttpStatus.NOT_FOUND);

    }

    // => 403
    @ExceptionHandler(UserNotInTheRoom.class)
    public ResponseEntity<Map<String, Object>> handleUserNotInTheRoom(UserNotInTheRoom ex) {
        return new ResponseEntity<>(buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage()),
                HttpStatus.FORBIDDEN);

    }

    // => 409
    @ExceptionHandler(DuplicateChatRoom.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateChatRoom(DuplicateChatRoom ex) {
        return new ResponseEntity<>(buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage()),
                HttpStatus.CONFLICT);
    }

    // => 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> HandleVaildationEceeptions(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";
        return new ResponseEntity<>(buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);

    }

    // => 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
