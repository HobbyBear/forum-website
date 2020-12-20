package com.blog.template.vo.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentElem {
    private String content;

    private String commentId;

    private Long userId;

    private String username;

    private String avatar;

    private Long createTime;

}
