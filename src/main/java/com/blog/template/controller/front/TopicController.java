package com.blog.template.controller.front;

import com.blog.template.common.annotation.UserLoginToken;
import com.blog.template.common.utils.UserUtil;
import com.blog.template.dao.TopicDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.topic.Topic;
import com.blog.template.vo.CreateTopicReq;
import com.blog.template.vo.ResponseMsg;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/topic")
public class TopicController {


    @Autowired
    private TopicDao topicDao;


    @ApiOperation("create topic")
    @PostMapping
    @UserLoginToken
    public ResponseMsg createTopic(@RequestBody CreateTopicReq createTopicReq) {
        if (topicDao.findByTitle(createTopicReq.getTitle()).isPresent()) {
            throw new CustomerException("the topic title has exited!");
        }

        Topic topic = Topic.builder()
                .userId(UserUtil.getUser().getId())
                .categoryId(createTopicReq.getCategoryId()).title(createTopicReq.getTitle()).build();

        topicDao.save(topic);

        return ResponseMsg.success200("create topic success");
    }

    @ApiOperation("update topic")
    @PutMapping
    @UserLoginToken
    public ResponseMsg updateTopic(@RequestBody CreateTopicReq createTopicReq) {
        Optional<Topic> topicOptional = topicDao.findById(createTopicReq.getTopicId());
        if (!topicOptional.isPresent()) {
            throw new CustomerException("the topic  has not exited!");
        }

        Topic topic = Topic.builder()
                .userId(UserUtil.getUser().getId())
                .categoryId(createTopicReq.getCategoryId()).title(createTopicReq.getTitle()).build();

        topicDao.save(topic);

        return ResponseMsg.success200("update topic success");
    }




}
