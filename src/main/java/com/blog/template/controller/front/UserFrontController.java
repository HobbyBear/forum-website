package com.blog.template.controller.front;

import com.blog.template.dao.UserDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.service.user.UserService;
import com.blog.template.vo.LoginRequest;
import com.blog.template.vo.RegPwdRequest;
import com.blog.template.vo.ResponseMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class UserFrontController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @PostMapping("/regByPwd")
    public ResponseMsg regByPwd(@RequestBody RegPwdRequest regPwdRequest) throws MessagingException {
        userService.regByPwd(regPwdRequest);
        return ResponseMsg.success200("register success");
    }

    @PostMapping("/login")
    public ResponseMsg login(@RequestBody LoginRequest loginRequest) throws MessagingException {
        Optional<UserInfo> userInfo = userDao.findByUsername(loginRequest.getUsername());
        if (!userInfo.isPresent()){
            throw new CustomerException("username find fail !");
        }
        if (!userInfo.get().getPassword().equals(loginRequest.getPwd())){
            throw new CustomerException("password find fail !");
        }
        return ResponseMsg.success200("login success");
    }



}
