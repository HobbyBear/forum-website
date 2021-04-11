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
    // 根据话题id获取答案列表
    public ResponseMsg answerList(@RequestParam Long topicId) {

        // 获取当前用户
        UserInfo currentUser = UserUtil.getUser();

        List<AnswerElemVo> resp = new ArrayList<>();

        // 根据topic id获取该topic下所有回答
        List<Answer> answerList = answerDao.findByTopicId(topicId);

        if (answerList.size() == 0) {
            return ResponseMsg.success200(resp);
        }

        // 获取回答用户id
        List<Long> userIdList = new ArrayList<>();
        answerList.forEach((answer) -> {
            userIdList.add(answer.getUserId());
        });

        // 根据id获取回答用户信息
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
            // 获取点赞记录
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
        // 封装回答列表
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
    // 创造答案
    public ResponseMsg createAnswer(@RequestBody CreateAnswerReq createAnswerReq) {

        // 获取话题
        Optional<Topic> optionalTopic = topicDao.findById(createAnswerReq.getTopicId());

        if (!optionalTopic.isPresent()) {
            throw new CustomerException("the topic id is in valid");
        }

        // 获取当前用户信息
        UserInfo currentUser = UserUtil.getUser();
        // 封装答题信息
        Answer answer = Answer.builder()
                .userId(UserUtil.getUser().getId())
                .content(createAnswerReq.getContent())
                .contentText(createAnswerReq.getContentText())
                .createTime(LocalDateTime.now())
                .userId(currentUser.getId())
                .topicId(createAnswerReq.getTopicId()).build();

        answerDao.save(answer);

        // 增加话题回答数量
        topicDao.incrAnswerNum(createAnswerReq.getTopicId());


        return ResponseMsg.success200("create answer success");
    }


    @ApiOperation("praise answer")
    @GetMapping("praise_answer")
    @UserLoginToken
    @Transactional
    // 点赞答案
    public ResponseMsg praiseAnswer(@RequestParam Long answerId) {
        // 获取当前用户
        UserInfo currentUser = UserUtil.getUser();
        // 获取当前用户是否点赞过这个答案的记录
        Optional<LikeRecord> likeRecordOptional =
                likeRecordDao.findByRecordTypeAndUserIdAndRecordId(Constant.LikeRecordType.ANSWER_LIKE_RECORD_TYPE, currentUser.getId(), answerId);

        Optional<Answer> answerOptional = answerDao.findById(answerId);
        if (!answerOptional.isPresent()){
            return ResponseMsg.fail400("answer not find");
        }

        // 如果之前记录是点赞过，那么此次操作就是取消点赞
        if (likeRecordOptional.isPresent() && likeRecordOptional.get().getIsLike()) {
            likeRecordOptional.get().setIsLike(false);
            answerDao.notLikeAnswer(answerId);
            userDao.decrPriaseNum(answerOptional.get().getUserId());
            likeRecordDao.save(likeRecordOptional.get());
            return ResponseMsg.success200(false);
        }

        // 如果之前是未点赞，那么此次操作就更新为点赞
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
        // 增加话题用户的点赞数量
        userDao.incrPriaseNum(answerOptional.get().getUserId());
        likeRecordDao.save(likeRecord);
        return ResponseMsg.success200(true);
    }

}
