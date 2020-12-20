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
public class TopicListResp {

    private int pageSize;

    private int currentPage;

    private int size;

    private int totalPage;

    private List<TopicListElemVo> topicListElemList;



}

