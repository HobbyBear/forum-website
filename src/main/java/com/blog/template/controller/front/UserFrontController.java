package com.blog.template.controller.front;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.blog.template.common.annotation.PassToken;
import com.blog.template.common.annotation.UserLoginToken;
import com.blog.template.common.utils.UserUtil;
import com.blog.template.dao.UserDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.models.userinfo.UserInfoVo;
import com.blog.template.service.user.UserService;
import com.blog.template.vo.LoginRequest;
import com.blog.template.vo.RegPwdRequest;
import com.blog.template.vo.ResponseMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@RestController
@RequestMapping("/user")
@Slf4j
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
    @PassToken
    public ResponseMsg login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws MessagingException {
        Optional<UserInfo> userInfo = userDao.findByUsername(loginRequest.getUsername());
        if (!userInfo.isPresent()){
            throw new CustomerException("username find fail !");
        }
        if (!userInfo.get().getPassword().equals(loginRequest.getPwd())){
            throw new CustomerException("password find fail !");
        }
        String token = getToken(userInfo.get());
        log.info(token);
        Cookie cookie=new Cookie("Authorization",token);
        cookie.setMaxAge(60* 60* 24);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseMsg.success200("login success");
    }

    public String getToken(UserInfo user) {
        String token="";
        token= JWT.create().withAudience(user.getId().toString())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    @GetMapping
    @UserLoginToken
    public ResponseMsg userInfo(){
        Long userId = UserUtil.getUserId();
        Optional<UserInfo> userInfo =  userDao.findById(userId);
        if (!userInfo.isPresent()){
            throw new CustomerException("user is not legal! please login in again");
        }
        UserInfoVo userInfoVo = UserInfoVo.builder().avatar(userInfo.get().getAvatar())
                .id(userInfo.get().getId())
                .username(userInfo.get().getUsername())
                .build();
        return ResponseMsg.success200(userInfoVo);
    }



}
