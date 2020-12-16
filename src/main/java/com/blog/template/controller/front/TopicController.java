package com.blog.template.controller.front;

import com.blog.template.dao.TopicDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.topic.Topic;
import com.blog.template.vo.CreateTopicReq;
import com.blog.template.vo.ResponseMsg;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic")
public class TopicController {


    @Autowired
    private TopicDao topicDao;


    @ApiOperation("create topic")
    @PostMapping
    public ResponseMsg createTopic(@RequestBody CreateTopicReq createTopicReq){
        if (topicDao.findByTitle(createTopicReq.getTitle()).isPresent()){
            throw new CustomerException("the topic title has exited!");
        }

        Topic topic = Topic.builder().categoryId(createTopicReq.getCategoryId()).title(createTopicReq.getTitle()).build();

        topicDao.save(topic);

        return ResponseMsg.success200("create topic success");
    }

}
