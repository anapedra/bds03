package com.devsuperior.bds03.controllers.excepitions;




import com.devsuperior.bds03.services.exceptions.DataBaseException;
import com.devsuperior.bds03.services.exceptions.ResorceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler{

    /*
        // Sem contrutor costrutor:
        @ExceptionHandler(ResorceNotFoundException.class)
        public ResponseEntity<StanderdErrer> resourceNotFound(ResorceNotFoundException e, HttpServletRequest request){
            StanderdErrer err=new StanderdErrer();
            err.setTimestamp(Instant.now());
            err.setStatus(HttpStatus.NO_CONTENT.value());
            err.setError("Resource not found");
            err.setMessage(e.getMessage());
            err.setPath(request.getRequestURI());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }

     */
    //Com construtor:
    @ExceptionHandler(ResorceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResorceNotFoundException e, HttpServletRequest request){
        String error = "Resource not found";
        HttpStatus status= HttpStatus.NOT_FOUND;
        StandardError errer=new StandardError(Instant.now(),status.value(),error, e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(errer);
    }
    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> dataBase(DataBaseException e, HttpServletRequest request){
        String error = "DataBase error";
        HttpStatus status= HttpStatus.BAD_REQUEST;
        StandardError errer=new StandardError(Instant.now(),status.value(),error, e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(errer);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
        String error = "Validation Exception";
        HttpStatus status= HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError errer=new ValidationError(Instant.now(),status.value(),error, e.getMessage(),request.getRequestURI());

        for(FieldError f:e.getBindingResult().getFieldErrors()){
            errer.addError(f.getField(), f.getDefaultMessage());

        }

        return ResponseEntity.status(status).body(errer);
    }


}
