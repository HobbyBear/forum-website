package com.blog.template.exceptions;

import com.blog.template.vo.ResponseMsg;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;


@RestControllerAdvice
public class ExceptionAdvice {



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseMsg handle(ConstraintViolationException e) {
        StringBuffer sb = new StringBuffer();
        e.getConstraintViolations().forEach(error ->{
            sb.append(error.getMessage()).append(";");
        });
        return ResponseMsg.fail400(sb.toString());
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMsg handle(MethodArgumentNotValidException e) {
        StringBuffer sb = new StringBuffer();
        e.getBindingResult().getAllErrors().forEach(error ->{
            sb.append(error.getDefaultMessage()).append(";");
        });
        return ResponseMsg.fail400(sb.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomerException.class)
    public ResponseMsg handel400(CustomerException e){
        return ResponseMsg.customer(e.getCode(),e.getMsg(),e.getData());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseMsg handel500(Exception e){
        e.printStackTrace();
        return ResponseMsg.fail500();
    }
}
