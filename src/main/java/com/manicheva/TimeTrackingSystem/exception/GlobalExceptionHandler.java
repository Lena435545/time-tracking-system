package com.manicheva.TimeTrackingSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView handleUsernameNotFound(UsernameNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("message", "User not found");
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgument(IllegalArgumentException ex) {
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("message", ex.getMessage());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex) {
        ModelAndView mav = new ModelAndView("auth/access_denied");
        mav.addObject("message", "Access denied");
        return mav;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("message", "An internal error occurred");
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("message", "An unexpected error occurred");
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }
}