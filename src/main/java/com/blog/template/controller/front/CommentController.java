package com.blog.template.controller.front;


import com.blog.template.common.annotation.PassToken;
import com.blog.template.common.annotation.UserLoginToken;
import com.blog.template.common.constants.Constant;
import com.blog.template.common.utils.UserUtil;
import com.blog.template.dao.AnswerDao;
import com.blog.template.dao.CommentDao;
import com.blog.template.dao.LikeRecordDao;
import com.blog.template.dao.UserDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.answer.Answer;
import com.blog.template.models.comment.Comment;
import com.blog.template.models.likerecord.LikeRecord;
import com.blog.template.models.userinfo.UserInfo;
import com.blog.template.vo.ResponseMsg;
import com.blog.template.vo.comment.CommentElemVo;
import com.blog.template.vo.comment.CreateCommentReq;
import com.blog.template.vo.user.UserElemVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private LikeRecordDao likeRecordDao;

    @Autowired
    private AnswerDao answerDao;

    @ApiOperation("comment list")
    @GetMapping("list")
    @PassToken
    public ResponseMsg commentList(@RequestParam(required = false) Long answerId) {

        UserInfo currentUser = UserUtil.getUser();

        List<CommentElemVo> resp = new ArrayList<>();

        List<Comment> commentList = new ArrayList<>();

        commentList = commentDao.findByAnswerId(answerId);


        if (commentList.size() == 0) {
            return ResponseMsg.success200(resp);
        }

        List<Long> userIdList = new ArrayList<>();
        commentList.forEach((comment) -> {
            userIdList.addAll(Arrays.asList(comment.getFromUserId(), comment.getToUserId()));
        });

        List<UserInfo> userInfoList = userDao.findByIdIn(userIdList);
        HashMap<Long, UserInfo> userMap = new HashMap<>();
        for (UserInfo u :
                userInfoList) {
            userMap.put(u.getId(), u);
        }
        List<Long> commentIdList = new ArrayList<>();
        commentList.forEach((comment) -> {
            commentIdList.add(comment.getId());
        });

        HashMap<Long, Boolean> likeRecordMap = new HashMap<>();
        if (currentUser != null) {
            List<LikeRecord> likeRecordList = likeRecordDao.
                    findByRecordTypeAndUserIdAndRecordIdIn(Constant.LikeRecordType.COMMENT_LIKE_RECORD_TYPE, currentUser.getId(),
                            commentIdList);
            likeRecordList.forEach(likeRecord -> {
                if (likeRecord.getIsLike()) {
                    likeRecordMap.put(likeRecord.getRecordId(), Boolean.TRUE);
                } else {
                    likeRecordMap.put(likeRecord.getRecordId(), Boolean.FALSE);
                }
            });

        }

        for (Comment comment :
                commentList) {
            UserInfo fromUser = userMap.get(comment.getFromUserId());
            UserInfo toUser = userMap.get(comment.getToUserId());
            CommentElemVo commentElemVo = CommentElemVo.builder()
                    .commentId(comment.getId())
                    .fromUer(UserElemVo.builder()
                            .avator(fromUser.getAvatar())
                            .userId(fromUser.getId())
                            .username(fromUser.getUsername())
                            .isSelf(currentUser != null && currentUser.getId().equals(fromUser.getId()))
                            .build())
                    .content(comment.getContent())
                    .createTime(comment.getCreateTime().toEpochSecond(ZoneOffset.UTC))
                    .toUser(UserElemVo.builder()
                            .avator(toUser.getAvatar())
                            .userId(toUser.getId())
                            .username(toUser.getUsername())
                            .isSelf(currentUser != null && currentUser.getId().equals(fromUser.getId()))
                            .build())
                    .answerId(comment.getAnswerId())
                    .Level(comment.getLevel())
                    .isLike(likeRecordMap.size() > 0 ? likeRecordMap.get(comment.getId()) : false)
                    .build();
            resp.add(commentElemVo);
        }

        return ResponseMsg.success200(resp);

    }


    @ApiOperation("add comment")
    @PostMapping("create_comment")
    @UserLoginToken
    public ResponseMsg createComment(@RequestBody CreateCommentReq createCommentReq) {

        if (createCommentReq.getCommentId() == 0 && createCommentReq.getAnswerId() == 0) {
            throw new CustomerException("comment id and answer id can not both zero");
        }

        if (createCommentReq.getContent().equals("")) {
            throw new CustomerException("comment content msut not be empty");
        }

        Long toUserId = 0L;
        int level = 0;

        if (createCommentReq.getCommentId() != 0) {
            Optional<Comment> commentOptional = commentDao.findById(createCommentReq.getCommentId());
            if (!commentOptional.isPresent()) {
                return ResponseMsg.success200("comment id is not exit ,please check your args");
            }
            toUserId = commentOptional.get().getFromUserId();
            level = 1;
        }

        if (createCommentReq.getCommentId() == 0) {
            Optional<Answer> answerOptional = answerDao.findById(createCommentReq.getAnswerId());
            if (!answerOptional.isPresent()) {
                return ResponseMsg.success200("comment id is not exit ,please check your args");
            }
            toUserId = answerOptional.get().getUserId();
        }

        if (UserUtil.getUser().getId().equals(toUserId)){
            return ResponseMsg.success200("you do not permit comment your self!");
        }


        Comment comment = Comment.builder()
                .fromUserId(UserUtil.getUser().getId())
                .toUserId(toUserId)
                .content(createCommentReq.getContent())
                .topicId(createCommentReq.getTopicId())
                .answerId(createCommentReq.getAnswerId())
                .level(level)
                .createTime(LocalDateTime.now())
                .parentCommentId(createCommentReq.getCommentId())
                .build();

        if (createCommentReq.getCommentId() != null && createCommentReq.getCommentId() != 0) {
            comment.setLevel(1);
            comment.setParentCommentId(createCommentReq.getCommentId());
        } else {
            comment.setLevel(0);
        }

        commentDao.save(comment);
        answerDao.incrCommentNum(createCommentReq.getAnswerId());
        return ResponseMsg.success200("create comment success");
    }


    @ApiOperation("praise comment")
    @GetMapping("praise_comment")
    @UserLoginToken
    @Transactional
    public ResponseMsg praiseAnswer(@RequestParam Long commentId) {
        UserInfo currentUser = UserUtil.getUser();
        Optional<LikeRecord> likeRecordOptional =
                likeRecordDao.findByRecordTypeAndUserIdAndRecordId(Constant.LikeRecordType.COMMENT_LIKE_RECORD_TYPE,
                        currentUser.getId(), commentId);
        if (likeRecordOptional.isPresent() && likeRecordOptional.get().getIsLike()) {
            likeRecordOptional.get().setIsLike(false);
            likeRecordDao.save(likeRecordOptional.get());
            return ResponseMsg.success200(false);
        }

        if (likeRecordOptional.isPresent() && !likeRecordOptional.get().getIsLike()) {
            likeRecordOptional.get().setIsLike(true);
            likeRecordDao.save(likeRecordOptional.get());
            return ResponseMsg.success200(true);
        }
        LikeRecord likeRecord = LikeRecord
                .builder()
                .isLike(true)
                .recordId(commentId)
                .recordType(Constant.LikeRecordType.COMMENT_LIKE_RECORD_TYPE)
                .userId(currentUser.getId())
                .build();
        likeRecordDao.save(likeRecord);
        return ResponseMsg.success200(true);
    }


}
