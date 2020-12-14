package com.blog.template.dao;

import com.blog.template.models.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TopicDao  extends JpaRepository<Topic,Long>, JpaSpecificationExecutor<Topic> {

    Optional<Topic> findById(Long id);

    Optional<Topic> findByTitle(String title);

}
