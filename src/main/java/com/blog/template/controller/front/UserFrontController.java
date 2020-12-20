package com.blog.template.controller.front;

import com.blog.template.common.annotation.PassToken;
import com.blog.template.common.annotation.UserLoginToken;
import com.blog.template.common.constants.Constant;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserFrontController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @PassToken
    @PostMapping("/regByPwd")
    public ResponseMsg regByPwd(@RequestBody RegPwdRequest regPwdRequest) throws MessagingException {
        userService.regByPwd(regPwdRequest);
        return ResponseMsg.success200("register success");
    }

    @PostMapping("/login")
    @PassToken
    public ResponseMsg login(@RequestBody LoginRequest loginRequest,
                             HttpServletResponse response, HttpSession session) throws MessagingException {
        Optional<UserInfo> userInfo = userDao.findByUsername(loginRequest.getUsername());
        if (!userInfo.isPresent()) {
            throw new CustomerException("username find fail !");
        }
        if (!userInfo.get().getPassword().equals(loginRequest.getPwd())) {
            throw new CustomerException("password find fail !");
        }

        session.setAttribute(Constant.SessionKey.SESSION_USERID,userInfo.get().getId());
        return ResponseMsg.success200("login success");
    }


    @GetMapping
    @UserLoginToken
    public ResponseMsg userInfo() {
        UserInfo userInfo = UserUtil.getUser();
        UserInfoVo userInfoVo = UserInfoVo
                .builder()
                .avatar(userInfo.getAvatar())
                .id(userInfo.getId())
                .username(userInfo.getUsername())
                .build();
        return ResponseMsg.success200(userInfoVo);
    }


}
