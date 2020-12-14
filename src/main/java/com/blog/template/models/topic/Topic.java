package com.blog.template.models.topic;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "topic")
public class Topic {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;


    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "is_delete")
    private boolean isDelete;

    @Column(name = "like_num")
    private int LikeNum;

    @Column(name = "dislike_num")
    private int dislikeNum;

    @Column(name = "comment_num")
    private int commentNum;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "category_id")
    private Long categoryId;
}
