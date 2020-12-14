package com.blog.template.service.user;

import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.vo.PageBean;
import com.blog.template.models.userinfo.UserInfoVo;
import com.blog.template.models.userinfo.UserSearchVo;
import com.blog.template.vo.RegPwdRequest;

import javax.mail.MessagingException;
import java.util.Optional;


public interface UserService {


    void regByPwd(RegPwdRequest regPwdRequest) throws MessagingException;


    void saveOrUpdateUser(UserInfo userInfo);

    void delUserById(Long id);


    Optional<UserInfo> findByUsername(String username);

    PageBean<UserInfoVo> findUserVoByUserSearchVo(UserSearchVo userSearchVo, Integer page, Integer pageSize);

}
