package com.blog.template.models.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "level")
    private int level;

    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "from_user_id")
    private Long fromUserId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "to_user_id")
    private Long toUserId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

}
