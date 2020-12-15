package com.blog.template.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerException extends RuntimeException {

    private Integer code;

    private String msg;

    private Object data;

    public CustomerException(Integer code, String msg ){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public CustomerException(String msg){
        super(msg);
        code = 400;
        this.msg = msg;

    }
    public CustomerException(Integer code,String msg,Object data){
        super(msg);
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
}
