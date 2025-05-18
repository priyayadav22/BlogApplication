package com.example.chaiAndPosts.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateTitleException.class)
    public ResponseEntity<Map<String,String>> handleDupliacteTitle(DuplicateTitleException e){
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMesssge" , e.getMessage());
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleAll(Exception e){
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMesssge" , "Something went wrong : " + e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoChangeDetectedException.class)
    public ResponseEntity<Map<String, String>> handleNoChange(NoChangeDetectedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }


}
