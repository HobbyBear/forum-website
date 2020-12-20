package com.blog.template.controller.front;


import com.blog.template.common.annotation.PassToken;
import com.blog.template.common.annotation.UserLoginToken;
import com.blog.template.common.constants.Constant;
import com.blog.template.common.utils.UserUtil;
import com.blog.template.dao.CommentDao;
import com.blog.template.dao.LikeRecordDao;
import com.blog.template.dao.UserDao;
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

    @ApiOperation("comment list")
    @PostMapping("list")
    @PassToken
    public ResponseMsg commentList(@RequestParam Long answerId, @RequestParam Long commentId, @RequestParam int level) {

        UserInfo currentUser = UserUtil.getUser();

        List<CommentElemVo> resp = new ArrayList<>();

        List<Comment> commentList = new ArrayList<>();

        if (level == 0) {
            commentList = commentDao.findByAnswerIdAndLevel(answerId, 0);
        }

        if (level == 1) {
            commentList = commentDao.findByParentCommentIdAndLevel(commentId, 1);
        }

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

        HashMap<Long,Boolean> likeRecordMap = new HashMap<>();
        if (currentUser != null){
            List<LikeRecord> likeRecordList = likeRecordDao.
                    findByRecordTypeAndUserIdAndRecordIdIn(Constant.LikeRecordType.COMMENT_LIKE_RECORD_TYPE,currentUser.getId(),
                            commentIdList);
            likeRecordList.forEach(likeRecord -> {
                if (likeRecord.getIsLike()){
                    likeRecordMap.put(likeRecord.getRecordId(), Boolean.TRUE);
                }else {
                    likeRecordMap.put(likeRecord.getRecordId(), Boolean.FALSE);
                }
            });

        }

        for (Comment comment :
                commentList) {
            UserInfo fromUser = userMap.get(comment.getFromUserId());
            UserInfo toUser = userMap.get(comment.getFromUserId());
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
                    .isLike(likeRecordMap.get(comment.getId()))
                    .build();
            resp.add(commentElemVo);
        }

        return ResponseMsg.success200(resp);

    }


    @ApiOperation("add comment")
    @PostMapping("create_comment")
    @UserLoginToken
    public ResponseMsg createComment(@RequestBody CreateCommentReq createCommentReq) {
        Comment comment = Comment.builder()
                .fromUserId(UserUtil.getUser().getId())
                .toUserId(createCommentReq.getToUserId())
                .content(createCommentReq.getContent())
                .topicId(createCommentReq.getTopicId())
                .answerId(createCommentReq.getAnswerId())
                .createTime(LocalDateTime.now())
                .build();

        if (createCommentReq.getCommentId() == null || createCommentReq.getCommentId() == 0) {
            comment.setLevel(1);
            comment.setParentCommentId(createCommentReq.getCommentId());
        }

        commentDao.save(comment);
        return ResponseMsg.success200("create comment success");
    }


    @ApiOperation("praise comment")
    @GetMapping("praise_comment")
    @UserLoginToken
    @Transactional
    public ResponseMsg praiseAnswer(@RequestParam Long commentId){
        UserInfo currentUser = UserUtil.getUser();
        Optional<LikeRecord> likeRecordOptional =
                likeRecordDao.findByRecordTypeAndUserIdAndRecordId(Constant.LikeRecordType.COMMENT_LIKE_RECORD_TYPE,
                        currentUser.getId(),commentId);
        if (likeRecordOptional.isPresent() && likeRecordOptional.get().getIsLike()){
            likeRecordOptional.get().setIsLike(false);
            likeRecordDao.save(likeRecordOptional.get());
            return ResponseMsg.success200(false);
        }

        if (likeRecordOptional.isPresent() && !likeRecordOptional.get().getIsLike()){
            likeRecordOptional.get().setIsLike(true);
            likeRecordDao.save(likeRecordOptional.get());
            return ResponseMsg.success200(true);
        }
        LikeRecord likeRecord = LikeRecord
                .builder()
                .isLike(true)
                .recordId(commentId)
                .recordType(Constant.LikeRecordType.ANSWER_LIKE_RECORD_TYPE)
                .userId(currentUser.getId())
                .build();
        likeRecordDao.save(likeRecord);
        return ResponseMsg.success200(true);
    }


}
