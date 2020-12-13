package com.blog.template.service;

import cn.hutool.core.bean.BeanUtil;
import com.blog.template.common.constants.Constant;
import com.blog.template.dao.RoleDao;
import com.blog.template.dao.UserDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author 19624
 */
@Service
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmai) throws UsernameNotFoundException {

        return null;
    }
}
