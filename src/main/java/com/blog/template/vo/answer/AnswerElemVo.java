package com.blog.template.vo.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerElemVo {
    private String content;

    private String contentText;

    private Long answerId;

    private Long userId;

    private String username;

    private String avatar;

    private Long createTime;

    private int likeNum;

    private int commentNum;

    private boolean isLike;
}
