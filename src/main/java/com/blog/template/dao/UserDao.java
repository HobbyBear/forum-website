package com.blog.template.dao;

import com.blog.template.models.userinfo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author 19624
 */
public interface UserDao extends JpaRepository<UserInfo,Long>,JpaSpecificationExecutor<UserInfo> {


    Optional<UserInfo> findByUsername(String username);


    /**
     * @Description: 删除5分钟内没有注册激活的用户
     * @Author: xch
     * @Date: 2019/6/19 17:27
    */
    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "DELETE FROM user_info  WHERE `status` = 2 AND create_time  <= date_add(now(),interval -5 MINUTE )")
    public void deleteUnInvokeUserInHalf5Minutes();


    List<UserInfo> findByIdIn(List<Long> userIds);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update forum.user_info  set praise_num = praise_num + 1  WHERE id = ? ")
    void incrPriaseNum(Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update forum.user_info  set praise_num = praise_num - 1  WHERE id = ? ")
    void decrPriaseNum(Long id);

}
