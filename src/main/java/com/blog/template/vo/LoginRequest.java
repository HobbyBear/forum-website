package com.blog.template.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {


    @NotBlank(message = "username can not empty")
    private String username;


    @NotBlank(message = "password can not empty")
    private String password;
}
