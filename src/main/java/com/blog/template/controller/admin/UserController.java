package com.blog.template.controller.admin;

import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.models.userinfo.UserSearchVo;
import com.blog.template.service.user.UserService;
import com.blog.template.vo.ResponseMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 对用户进行增删查改
 *
 * @author xch
 * @since 2019/6/18 16:42
 **/
@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    // 更新用户信息
    public ResponseMsg updateOrSaveUser(@RequestBody UserInfo userInfo) {
        if (userInfo.getId() == null) {
            userInfo.setCreateTime(LocalDateTime.now());
        }
        userService.saveOrUpdateUser(userInfo);
        return ResponseMsg.success200("请求成功");
    }



    @DeleteMapping("/{userId}")
    // 根据用户id删除用户
    public ResponseMsg delUserById(@PathVariable("userId") Long userId) {
        userService.delUserById(userId);
        return ResponseMsg.success200("请求成功");
    }


    @GetMapping()
    // 分页模糊搜索用户信息
    public ResponseMsg findUserList(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    UserSearchVo userSearchVo){
        return ResponseMsg.success200(userService.findUserVoByUserSearchVo(userSearchVo,page,pageSize));
    }

}
