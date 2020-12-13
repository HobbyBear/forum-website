package com.blog.template.controller.front;

import com.blog.template.service.user.UserService;
import com.blog.template.vo.RegPwdRequest;
import com.blog.template.vo.ResponseMsg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: xch
 * @create: 2019-06-15 22:49
 **/
@RestController
public class RegController {

    @Autowired
    private UserService userService;

    /**
    * @Description: 邮件注册
    * @Author: xch
    * @Date: 2019/6/15
    */
    @PostMapping("/regByPwd")
    public ResponseMsg regByPwd(@RequestBody RegPwdRequest regPwdRequest) throws MessagingException {
        userService.regByPwd(regPwdRequest);
        return ResponseMsg.success200("发送邮件成功，请激活");
    }

}
