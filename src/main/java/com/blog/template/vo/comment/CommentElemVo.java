package com.blog.template.vo.comment;

import com.blog.template.vo.user.UserElemVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentElemVo {
    private String content;

    private Long commentId;

    private UserElemVo fromUer;

    private UserElemVo toUser;

    private Long answerId;

    private Long createTime;

    private int Level;

    private boolean isLike;
}
