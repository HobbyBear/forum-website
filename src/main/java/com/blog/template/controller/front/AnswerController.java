package com.blog.template.controller.front;


import com.blog.template.common.annotation.PassToken;
import com.blog.template.common.annotation.UserLoginToken;
import com.blog.template.common.constants.Constant;
import com.blog.template.common.utils.UserUtil;
import com.blog.template.dao.AnswerDao;
import com.blog.template.dao.LikeRecordDao;
import com.blog.template.dao.TopicDao;
import com.blog.template.dao.UserDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.answer.Answer;
import com.blog.template.models.likerecord.LikeRecord;
import com.blog.template.models.topic.Topic;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.vo.ResponseMsg;
import com.blog.template.vo.answer.AnswerElemVo;
import com.blog.template.vo.answer.CreateAnswerReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private LikeRecordDao likeRecordDao;

    @ApiOperation("answer list")
    @GetMapping("list")
    @PassToken
    public ResponseMsg answerList(@RequestParam Long topicId) {

        UserInfo currentUser = UserUtil.getUser();

        List<AnswerElemVo> resp = new ArrayList<>();

        List<Answer> answerList = answerDao.findByTopicId(topicId);

        if (answerList.size() == 0) {
            return ResponseMsg.success200(resp);
        }

        List<Long> userIdList = new ArrayList<>();
        answerList.forEach((answer) -> {
            userIdList.add(answer.getUserId());
        });

        List<UserInfo> userInfoList = userDao.findByIdIn(userIdList);
        HashMap<Long, UserInfo> userMap = new HashMap<>();
        for (UserInfo u :
                userInfoList) {
            userMap.put(u.getId(), u);
        }

        List<Long> answerIdList = new ArrayList<>();
        answerList.forEach((answer) -> {
            answerIdList.add(answer.getId());
        });

        HashMap<Long, Boolean> likeRecordMap = new HashMap<>();
        if (currentUser != null) {
            List<LikeRecord> likeRecordList = likeRecordDao.
                    findByRecordTypeAndUserIdAndRecordIdIn(Constant.LikeRecordType.ANSWER_LIKE_RECORD_TYPE, currentUser.getId(),
                            answerIdList);
            likeRecordList.forEach(likeRecord -> {
                if (likeRecord.getIsLike()) {
                    likeRecordMap.put(likeRecord.getRecordId(), Boolean.TRUE);
                } else {
                    likeRecordMap.put(likeRecord.getRecordId(), Boolean.FALSE);
                }
            });

        }
        for (Answer answer :
                answerList) {
            UserInfo answerUser = userMap.get(answer.getUserId());
            AnswerElemVo answerElemVo = AnswerElemVo.builder()
                    .answerId(answer.getId())
                    .avatar(answerUser.getAvatar())
                    .content(answer.getContent())
                    .contentText(answer.getContentText())
                    .createTime(answer.getCreateTime().toEpochSecond(ZoneOffset.UTC))
                    .username(answerUser.getUsername())
                    .commentNum(answer.getCommentNum())
                    .likeNum(answer.getLikeNum())
                    .isLike(likeRecordMap.size() > 0 && likeRecordMap.get(answer.getId()) != null && likeRecordMap.get(answer.getId()))
                    .build();
            resp.add(answerElemVo);
        }

        return ResponseMsg.success200(resp);

    }


    @ApiOperation("create answer")
    @PostMapping
    @UserLoginToken
    public ResponseMsg createAnswer(@RequestBody CreateAnswerReq createAnswerReq) {

        Optional<Topic> optionalTopic = topicDao.findById(createAnswerReq.getTopicId());

        if (!optionalTopic.isPresent()) {
            throw new CustomerException("the topic id is in valid");
        }

        UserInfo currentUser = UserUtil.getUser();
        Answer answer = Answer.builder()
                .userId(UserUtil.getUser().getId())
                .content(createAnswerReq.getContent())
                .contentText(createAnswerReq.getContentText())
                .createTime(LocalDateTime.now())
                .userId(currentUser.getId())
                .topicId(createAnswerReq.getTopicId()).build();

        answerDao.save(answer);

        topicDao.incrAnswerNum(createAnswerReq.getTopicId());


        return ResponseMsg.success200("create answer success");
    }


    @ApiOperation("praise answer")
    @GetMapping("praise_answer")
    @UserLoginToken
    @Transactional
    public ResponseMsg praiseAnswer(@RequestParam Long answerId) {
        UserInfo currentUser = UserUtil.getUser();
        Optional<LikeRecord> likeRecordOptional =
                likeRecordDao.findByRecordTypeAndUserIdAndRecordId(Constant.LikeRecordType.ANSWER_LIKE_RECORD_TYPE, currentUser.getId(), answerId);

        Optional<Answer> answerOptional = answerDao.findById(answerId);
        if (!answerOptional.isPresent()){
            return ResponseMsg.fail400("answer not find");
        }

        if (likeRecordOptional.isPresent() && likeRecordOptional.get().getIsLike()) {
            likeRecordOptional.get().setIsLike(false);
            answerDao.notLikeAnswer(answerId);
            userDao.decrPriaseNum(answerOptional.get().getUserId());
            likeRecordDao.save(likeRecordOptional.get());
            return ResponseMsg.success200(false);
        }

        if (likeRecordOptional.isPresent() && !likeRecordOptional.get().getIsLike()) {
            likeRecordOptional.get().setIsLike(true);
            answerDao.likeAnswer(answerId);
            userDao.incrPriaseNum(answerOptional.get().getUserId());
            likeRecordDao.save(likeRecordOptional.get());
            return ResponseMsg.success200(true);
        }
        LikeRecord likeRecord = LikeRecord
                .builder()
                .isLike(true)
                .recordId(answerId)
                .recordType(Constant.LikeRecordType.ANSWER_LIKE_RECORD_TYPE)
                .userId(currentUser.getId())
                .build();
        answerDao.likeAnswer(answerId);
        userDao.incrPriaseNum(answerOptional.get().getUserId());
        likeRecordDao.save(likeRecord);
        return ResponseMsg.success200(true);
    }

}
