package com.blog.template.service.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.blog.template.common.utils.PageBeanConvertUtil;
import com.blog.template.common.utils.UserUtil;
import com.blog.template.dao.UserDao;
import com.blog.template.exceptions.CustomerException;
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


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;





    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regByPwd(RegPwdRequest regPwdRequest) throws MessagingException {
        // 查看用户是否存在
        if (userDao.findByUsername(regPwdRequest.getUsername()).isPresent()) {
            throw new CustomerException("username has exited !");
        }
        UserInfo userInfo = UserInfo
                .builder()
                .password(regPwdRequest.getPwd())
                .username(regPwdRequest.getUsername())
                .createTime(LocalDateTime.now())
                .avatar(UserUtil.randomAvatorUrl())
                .praiseNum(0)
                .build();
        // 添加用户
        userDao.save(userInfo);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateUser(UserInfo userInfo) {
        if (userInfo.getId() != null) {
            // 查询用户信息
            UserInfo metaUser = userDao.getOne(userInfo.getId());
            // 复制用户实体信息
            BeanUtil.copyProperties(userInfo, metaUser, CopyOptions.create().setIgnoreNullValue(true));
            // 更新用户信息
            userDao.save(metaUser);
        } else {
            userDao.save(userInfo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delUserById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public Optional<UserInfo> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public PageBean<UserInfoVo> findUserVoByUserSearchVo(UserSearchVo userSearchVo, Integer page, Integer pageSize) {

        // 构造搜索对象
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
        // jpa 分页
        Page<UserInfo> userInfoPage = userDao.findAll(specification, PageRequest.of(page - 1, pageSize));
        PageBean pageBean = PageBeanConvertUtil.convertCustomerPageBean(userInfoPage);
        List<UserInfoVo> userInfoVos = Lists.newArrayList();

        // 封装分页结果
        pageBean.getContent().forEach(userInfo -> {
            UserInfo u = (UserInfo) userInfo;
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtil.copyProperties(u, userInfoVo);
            if (userInfoVo.getSource() != null) {
                userInfoVo.setSupportChangeRole(false);
            } else {
                userInfoVo.setSupportChangeRole(true);
            }
            userInfoVos.add(userInfoVo);
        });
        pageBean.setContent(userInfoVos);
        return pageBean;
    }


}
