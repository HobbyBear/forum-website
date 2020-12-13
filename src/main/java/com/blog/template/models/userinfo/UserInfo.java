package com.blog.template.models.userinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 19624
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_info")
public class UserInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;


    /**
     * 1,激活  0 -未激活
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 头像地址
     */
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
