package com.blog.template.dao;

import com.blog.template.models.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TopicDao  extends JpaRepository<Topic,Long>, JpaSpecificationExecutor<Topic> {

    Optional<Topic> findById(Long id);

    Optional<Topic> findByTitle(String title);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value ="update forum.topic set answer_num = answer_num + 1 where id = ?")
    void incrAnswerNum(Long topicId);

}
