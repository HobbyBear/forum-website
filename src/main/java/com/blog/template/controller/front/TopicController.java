package com.blog.template.controller.front;

import com.blog.template.dao.TopicDao;
import com.blog.template.models.topic.Topic;
import com.blog.template.vo.CreateTopicReq;
import com.blog.template.vo.ResponseMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("topic")
public class TopicController {

    @Autowired
    private TopicDao topicDao;

    @PostMapping
    public ResponseMsg CreateTopic(@RequestBody CreateTopicReq createTopicReq){
       if ( topicDao.findByTitle(createTopicReq.getTitle()).isPresent()){
           return ResponseMsg.fail400("the topic title has exited!");
       }

       // todo 用户id ,创建时间
       topicDao.save(Topic.builder().categoryId(createTopicReq.getCategoryId()).title(createTopicReq.getTitle()).build());

       return ResponseMsg.success200("add success");
    }

    @PostMapping
    public void UpdateTopic(@RequestBody CreateTopicReq createTopicReq){



    }
}
