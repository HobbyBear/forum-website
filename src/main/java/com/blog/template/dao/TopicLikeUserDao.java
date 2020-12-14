package com.blog.template.dao;

import com.blog.template.models.topiclikeuser.TopicLikeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TopicLikeUserDao extends JpaRepository<TopicLikeUser,Long>, JpaSpecificationExecutor<TopicLikeUser> {
}
