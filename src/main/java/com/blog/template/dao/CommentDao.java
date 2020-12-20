package com.blog.template.dao;

import com.blog.template.models.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment,Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findByAnswerId(Long answerId);

    List<Comment> findByAnswerIdAndLevel(Long answerId, int level);

    List<Comment> findByParentCommentIdAndLevel(Long parentCommentId, int level);

}
