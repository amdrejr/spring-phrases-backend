package com.amdrejr.phrases.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.amdrejr.phrases.exceptions.customExceptions.PhrasesErrorException;
import com.amdrejr.phrases.exceptions.customExceptions.UserInexistentOrInvalidPassword;
import com.amdrejr.phrases.exceptions.customExceptions.UsernameAlreadyExistsExceprion;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserInexistentOrInvalidPassword.class})
    @ResponseBody
    public ResponseEntity<StandardError> userOrInvalidPassword(UserInexistentOrInvalidPassword err, HttpServletRequest req) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String errorMsg = "User not found or invalid password";
        StandardError re = new StandardError(Instant.now(), status.value(), errorMsg, err.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(re);
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<StandardError> accessDenied(AccessDeniedException err, HttpServletRequest req) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String errMsg = "Insufficient permissions!";
        StandardError re = new StandardError(Instant.now(), status.value(), err.getMessage(), errMsg, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(re);
    }

    @ExceptionHandler({InsufficientAuthenticationException.class})
    @ResponseBody
    public ResponseEntity<StandardError> insufficientAuthentication(InsufficientAuthenticationException err, HttpServletRequest req) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String errMsg = "Insufficient authentication!";
        StandardError re = new StandardError(Instant.now(), status.value(), errMsg, err.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);
    }
    
    @ExceptionHandler({UsernameAlreadyExistsExceprion.class})
    @ResponseBody
    public ResponseEntity<StandardError> usernameAlreadyExists(UsernameAlreadyExistsExceprion err, HttpServletRequest req) {
        HttpStatus status = HttpStatus.CONFLICT;
        String errMsg = "Error create user.";
        StandardError re = new StandardError(Instant.now(), status.value(), errMsg, err.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(re);
    }
    
    @ExceptionHandler({PhrasesErrorException.class})
    @ResponseBody
    public ResponseEntity<StandardError> phrasesError(PhrasesErrorException err, HttpServletRequest req) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String errMsg = "Error phrases.";
        StandardError re = new StandardError(Instant.now(), status.value(), errMsg, err.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(re);
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ResponseEntity<StandardError> runtimeExeption(RuntimeException err, HttpServletRequest req) {
        if(err.getMessage() != null) {
            if(err.getMessage().contains("User not found")) {
                HttpStatus status = HttpStatus.NOT_FOUND;
                String errMsg = "Not found";
                StandardError re = new StandardError(Instant.now(), status.value(), errMsg, err.getMessage(), req.getRequestURI());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(re);
            }
        }

        System.out.println("------------ erro generico final ------------");
        err.printStackTrace();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errMsg = "Internal server error";
        StandardError re = new StandardError(Instant.now(), status.value(), errMsg, err.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re);
    }
}