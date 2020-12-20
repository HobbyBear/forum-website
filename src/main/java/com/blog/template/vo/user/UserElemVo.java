package com.blog.template.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserElemVo {

    private Long userId;

    private String username;

    private String avator;

    private boolean isSelf;
}
