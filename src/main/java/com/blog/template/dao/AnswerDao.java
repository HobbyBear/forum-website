package com.blog.template.dao;

import com.blog.template.models.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AnswerDao extends JpaRepository<Answer, Long>, JpaSpecificationExecutor<Answer> {

    List<Answer> findByTopicIdIn(List<Long> topicIdList);

    List<Answer> findByTopicId(Long topicId);

    @Modifying
    @Query(nativeQuery = true, value = "update forum.answer set like_num = like_num -1 where id =?")
    void notLikeAnswer(Long answerId);


    @Modifying
    @Query(nativeQuery = true, value = "update forum.answer set like_num = like_num +1 where id =?")
    void likeAnswer(Long answerId);

}
