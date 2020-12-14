package com.blog.template.controller.front;

import com.blog.template.service.user.UserService;
import com.blog.template.vo.RegPwdRequest;
import com.blog.template.vo.ResponseMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;


@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/regByPwd")
    public ResponseMsg regByPwd(@RequestBody RegPwdRequest regPwdRequest) throws MessagingException {
        userService.regByPwd(regPwdRequest);
        return ResponseMsg.success200("login success");
    }



}
