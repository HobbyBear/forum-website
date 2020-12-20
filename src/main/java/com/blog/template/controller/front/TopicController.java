package com.blog.template.controller.front;

import com.blog.template.common.annotation.PassToken;
import com.blog.template.common.annotation.UserLoginToken;
import com.blog.template.common.utils.UserUtil;
import com.blog.template.dao.AnswerDao;
import com.blog.template.dao.TopicDao;
import com.blog.template.dao.UserDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.topic.Topic;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.vo.ResponseMsg;
import com.blog.template.vo.topic.CreateTopicReq;
import com.blog.template.vo.topic.TopicListElemVo;
import com.blog.template.vo.topic.TopicListReq;
import com.blog.template.vo.topic.TopicListResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topic")
public class TopicController {


    @Autowired
    private TopicDao topicDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao commentDao;


    @ApiOperation("topic")
    @GetMapping
    @PassToken
    public ResponseMsg topic(@RequestParam Long topicId) {
        Optional<Topic> topic = topicDao.findById(topicId);
        if (!topic.isPresent()){
            throw new CustomerException("the topic title has not exited!");
        }

        Optional<UserInfo> userInfoOptional = userDao.findById(topic.get().getUserId());

        if (!userInfoOptional.isPresent()){
            throw new CustomerException("the user has not exited!");
        }

        TopicListElemVo topicListElem = TopicListElemVo.builder()
                .topicId(topic.get().getId())
                .avatar(userInfoOptional.get().getAvatar())
                .topicName(topic.get().getTitle())
                .createTime(topic.get().getCreateTime().toEpochSecond(ZoneOffset.UTC))
                .username(userInfoOptional.get().getUsername())
                .answerNum(topic.get().getAnswerNum())
                .build();

        return ResponseMsg.success200(topicListElem);
    }


    @ApiOperation("create topic")
    @PostMapping
    @UserLoginToken
    public ResponseMsg createTopic(@RequestBody CreateTopicReq createTopicReq) {
        if (topicDao.findByTitle(createTopicReq.getTitle()).isPresent()) {
            throw new CustomerException("the topic title has exited!");
        }

        Topic topic = Topic.builder()
                .userId(UserUtil.getUser().getId())
                .categoryId(createTopicReq.getCategoryId()).title(createTopicReq.getTitle())
                .createTime(LocalDateTime.now())
                .build();

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


    @ApiOperation("topic list")
    @PostMapping("list")
    @PassToken
    public ResponseMsg topicList(@RequestBody TopicListReq topicListReq) {

        TopicListResp topicListResp = TopicListResp
                .builder().
                currentPage(topicListReq.getPageNum())
                .pageSize(10)
                .size(0)
                .topicListElemList(new ArrayList<>())
                .build();


        Pageable pageable = PageRequest.of(topicListReq.getPageNum(), 10);
        Specification<Topic> specification = (Specification<Topic>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (topicListReq.getCategoryId() != 0) {
                Predicate p1 = criteriaBuilder.equal(root.get("categoryId"), topicListReq.getCategoryId());
                list.add(p1);
            }
            if (!topicListReq.getTopicName().equals("")) {
                Predicate p2 = criteriaBuilder.like(root.get("title"), "%" + topicListReq.getTopicName() + "%");
                list.add(p2);
            }
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };

        Page<Topic> topicPage = topicDao.findAll(specification, pageable);

        List<Topic> topicList = topicPage.getContent();

        List<Long> userIdList = new ArrayList<>();


        for (Topic topic :
                topicList) {
            userIdList.add(topic.getUserId());
        }

        if (topicList.size() == 0) {
            return ResponseMsg.success200(topicListResp);
        }

        topicListResp.setSize(topicList.size());


        List<UserInfo> userInfoList = userDao.findByIdIn(userIdList);
        HashMap<Long, UserInfo> userMap = new HashMap<>();
        for (UserInfo u :
                userInfoList) {
            userMap.put(u.getId(), u);
        }

        for (Topic topic :
                topicList) {
            UserInfo topicUser = userMap.get(topic.getUserId());
            TopicListElemVo topicListElem = TopicListElemVo.builder()
                    .topicId(topic.getId())
                    .avatar(topicUser.getAvatar())
                    .topicName(topic.getTitle())
                    .createTime(topic.getCreateTime().toEpochSecond(ZoneOffset.UTC))
                    .username(topicUser.getUsername())
                    .answerNum(topic.getAnswerNum())
                    .build();
            topicListResp.getTopicListElemList().add(topicListElem);
        }
        topicListResp.setTotalPage(topicListResp.getTotalPage());
        return ResponseMsg.success200(topicListResp);

    }




//
//    @ApiOperation("topic list with answer")
//    @PostMapping("list_with_answer")
//    @PassToken
//    public ResponseMsg topicListWithAnswer(@RequestBody TopicListReq topicListReq) {
//
//        TopicListResp topicListResp = TopicListResp.builder().topicListElemList(new ArrayList<>()).
//                currentPage(topicListReq.getPageNum()).pageSize(10).size(0).build();
//
//
//        Pageable pageable = PageRequest.of(topicListReq.getPageNum(), 10);
//        Specification<Topic> specification = (Specification<Topic>) (root, query, criteriaBuilder) -> {
//            List<Predicate> list = new ArrayList<>();
//            if (topicListReq.getCategoryId() != 0) {
//                Predicate p1 = criteriaBuilder.equal(root.get("categoryId"), topicListReq.getCategoryId());
//                list.add(p1);
//            }
//            if (!topicListReq.getTopicName().equals("")) {
//                Predicate p2 = criteriaBuilder.like(root.get("title"), "%" + topicListReq.getTopicName() + "%");
//                list.add(p2);
//            }
//            return criteriaBuilder.and(list.toArray(new Predicate[0]));
//        };
//
//        Page<Topic> topicPage = topicDao.findAll(specification, pageable);
//
//        List<Topic> topicList = topicPage.getContent();
//
//        List<Long> userIdList = new ArrayList<>();
//        List<Long> topicIdList = new ArrayList<>();
//        HashMap<Long, List<Answer>> topicAnswerListMap = new HashMap<>();
//
//
//        for (Topic topic :
//                topicList) {
//            userIdList.add(topic.getUserId());
//            topicIdList.add(topic.getId());
//        }
//
//        if (topicIdList.size() == 0) {
//            return ResponseMsg.success200(topicListResp);
//        }
//
//        topicListResp.setSize(topicIdList.size());
//
//        List<Answer> answerList = commentDao.findByTopicIdIn(topicIdList);
//
//        for (Answer answer :
//                answerList) {
//            userIdList.add(answer.getUserId());
//            List<Answer> commentListByTopicId = topicAnswerListMap.get(answer.getTopicId());
//            if (commentListByTopicId == null) {
//                commentListByTopicId = new ArrayList<>();
//            }
//            commentListByTopicId.add(answer);
//            topicAnswerListMap.put(answer.getTopicId(),commentListByTopicId);
//        }
//
//        List<UserInfo> userInfoList = userDao.findByIdIn(userIdList);
//        HashMap<Long, UserInfo> userMap = new HashMap<>();
//        for (UserInfo u :
//                userInfoList) {
//            userMap.put(u.getId(), u);
//        }
//
//        for (Topic topic :
//                topicList) {
//            UserInfo topicUser = userMap.get(topic.getUserId());
//            TopicListElemVo topicListElem = TopicListElemVo.builder()
//                    .topicId(topic.getId())
//                    .avatar(topicUser.getAvatar())
//                    .topicName(topic.getTitle())
//                    .createTime(topic.getCreateTime().toEpochSecond(ZoneOffset.UTC))
//                    .username(topicUser.getUsername())
//                    .build();
//
//            List<Answer> answers = topicAnswerListMap.get(topic.getId());
//
//            for (Answer answer :
//                    answers) {
//                UserInfo commentUser = userMap.get(answer.getUserId());
//                topicListElem.getCommentElemList().add(CommentElem.builder()
//                        .username(commentUser.getUsername())
//                        .userId(commentUser.getId())
//                        .avatar(commentUser.getAvatar())
//                        .createTime(answer.getCreateTime().toEpochSecond(ZoneOffset.UTC))
//                        .content(answer.getContentText().length() > 100 ? answer.getContentText().substring(0, 100) : answer.getContentText())
//                        .build());
//            }
//
//            topicListResp.getTopicListElemList().add(topicListElem);
//        }
//
//        return ResponseMsg.success200(topicListResp);
//
//    }
//


}
