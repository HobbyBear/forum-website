package com.blog.template.dao;

import com.blog.template.BaseTest;
import com.blog.template.models.category.Category;
import com.blog.template.models.answer.Answer;
import com.blog.template.models.comment.Comment;
import com.blog.template.models.topic.Topic;
import com.blog.template.models.userinfo.UserInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xch
 * @since 2019/6/18 16:52
 **/
public class DaoTest extends BaseTest {


    @Autowired
    private UserDao userDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private CommentDao commentDao;

    @Test
    public void initData() {
        userDao.save(UserInfo.builder()
                .password("xch")
                .username("xch")
                .createTime(LocalDateTime.now())
                .build());

        categoryDao.save(Category.builder()
                .name("study")
                .build());
        categoryDao.save(Category.builder()
                .name("live")
                .build());
        categoryDao.save(Category.builder()
                .name("family")
                .build());
        categoryDao.save(Category.builder()
                .name("work")
                .build());
        categoryDao.save(Category.builder()
                .name("life")
                .build());
        categoryDao.save(Category.builder()
                .name("else")
                .build());

        topicDao.save(Topic.builder()
                .categoryId(1L)
                .title("my topic")
                .createTime(LocalDateTime.now())
                .userId(1L)
                .build());

        answerDao.save(Answer.builder()
                .userId(1L)
                .topicId(1L)
                .content("hello world!")
                .contentText("hello world!")
                .createTime(LocalDateTime.now())
                .build());
        answerDao.save(Answer.builder()
                .userId(1L)
                .topicId(1L)
                .content("hello world2!")
                .contentText("hello world2!")
                .createTime(LocalDateTime.now())
                .build());
        commentDao.save(Comment.builder()
                .level(0)
                .topicId(1L)
                .content("comment hello world")
                .createTime(LocalDateTime.now())
                .answerId(1L)
                .fromUserId(1L)
                .toUserId(1L)
                .build());

    }

    @Test
    public void selData() {
        List<UserInfo> userInfos = userDao.findAll();
        System.out.println(userInfos);

        List<Comment> comments = commentDao.findAll();
        System.out.println(comments);

    }


}
