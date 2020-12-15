package com.blog.template.service.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.blog.template.common.constants.Constant;
import com.blog.template.common.utils.PageBeanConvertUtil;
import com.blog.template.dao.RoleDao;
import com.blog.template.dao.UserDao;
import com.blog.template.dao.UserRoleDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.role.Role;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.models.userinfo.UserInfoVo;
import com.blog.template.models.userinfo.UserSearchVo;
import com.blog.template.vo.PageBean;
import com.blog.template.vo.RegPwdRequest;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;


    @Autowired
    private RoleDao roleDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regByPwd(RegPwdRequest regPwdRequest) throws MessagingException {
        if (userDao.findByUsername(regPwdRequest.getUsername()).isPresent()) {
            throw new CustomerException("username has exited !");
        }
        UserInfo userInfo = UserInfo
                .builder()
                .password(regPwdRequest.getPwd())
                .username(regPwdRequest.getUsername())
                .status(Constant.USER.NOT_INVOKE)
                .createTime(LocalDateTime.now())
                .avatar("")
                .build();
        userDao.save(userInfo);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateUser(UserInfo userInfo) {
        if (userInfo.getId() != null) {
            UserInfo metaUser = userDao.getOne(userInfo.getId());
            BeanUtil.copyProperties(userInfo, metaUser, CopyOptions.create().setIgnoreNullValue(true));
            userDao.save(metaUser);
        } else {
            userDao.save(userInfo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delUserById(Long id) {
        userDao.deleteById(id);
        userRoleDao.deleteAllByUserId(id);
    }

    @Override
    public Optional<UserInfo> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public PageBean<UserInfoVo> findUserVoByUserSearchVo(UserSearchVo userSearchVo, Integer page, Integer pageSize) {
        Specification<UserInfo> specification = (Specification<UserInfo>) (root, criteriaQuery, cb) -> {
            List<Predicate> predicateList = Lists.newArrayList();
            if (userSearchVo.getUsername() != null) {
                predicateList.add(cb.like(root.get("username"), "%" + userSearchVo.getUsername() + "%"));
            }
            if (userSearchVo.getEndTime() != null) {
                predicateList.add(cb.lessThanOrEqualTo(root.get("createTime"), userSearchVo.getEndTime()));
            }
            if (userSearchVo.getStartTime() != null) {
                predicateList.add(cb.greaterThanOrEqualTo(root.get("createTime"), userSearchVo.getStartTime()));
            }
            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
        Page<UserInfo> userInfoPage = userDao.findAll(specification, PageRequest.of(page - 1, pageSize));
        PageBean pageBean = PageBeanConvertUtil.convertCustomerPageBean(userInfoPage);
        List<UserInfoVo> userInfoVos = Lists.newArrayList();
        pageBean.getContent().forEach(userInfo -> {
            UserInfo u = (UserInfo) userInfo;
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtil.copyProperties(u, userInfoVo);
            if (userInfoVo.getSource() != null) {
                userInfoVo.setSupportChangeRole(false);
            } else {
                userInfoVo.setSupportChangeRole(true);
            }
            userInfoVo.setRoles(roleDao.findRoleListByUserId(u.getId()).stream().map(Role::getRoleName).collect(Collectors.toList()));
            userInfoVos.add(userInfoVo);
        });
        pageBean.setContent(userInfoVos);
        return pageBean;
    }


}
