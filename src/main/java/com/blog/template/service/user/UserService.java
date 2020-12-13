package com.blog.template.service.user;

import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.vo.PageBean;
import com.blog.template.models.userinfo.UserInfoVo;
import com.blog.template.models.userinfo.UserSearchVo;
import com.blog.template.vo.RegPwdRequest;

import javax.mail.MessagingException;
import java.util.Optional;

/**
 * @author 19624
 */
public interface UserService {

    /**
    * @Description: 通过邮件注册
    * @Author: xch
    * @Date: 2019/6/16
    */
    void regByPwd(RegPwdRequest regPwdRequest) throws MessagingException;


    /**
     * @Author xch
     * @Description  插入或更新用户,支持动态更新
     * @Date 16:45 2019/6/18
     * @Param [userInfo]
     * @return void
     **/
    void saveOrUpdateUser(UserInfo userInfo);

    /**
     * @Author xch
     * @Description  根据id删除用户
     * @Date 16:46 2019/6/18
     * @Param [idList]
     * @return void
     **/
    void delUserById(Long id);

    /**
     * @Description: 根据姓名查找用户
     * @Author: xch
     * @Date: 2019/6/21 8:33
    */
    Optional<UserInfo> findByUsername(String username);

    PageBean<UserInfoVo> findUserVoByUserSearchVo(UserSearchVo userSearchVo, Integer page, Integer pageSize);

}
