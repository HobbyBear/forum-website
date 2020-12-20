package com.blog.template.vo.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentReq {

    private String content;

    private Long toUserId;

    private Long topicId;

    private Long answerId;

    private Long commentId;
}
