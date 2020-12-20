package com.blog.template.vo.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicListElemVo {
    private String topicName;

    private Long topicId;

    private Long userId;

    private String username;

    private String avatar;

    private Long createTime;

    private List<CommentElem> commentElemList;

    private int answerNum;

}
