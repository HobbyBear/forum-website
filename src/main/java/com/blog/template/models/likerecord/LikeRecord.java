package com.blog.template.models.likerecord;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "like_record")
public class  LikeRecord {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_like")
    private Boolean isLike;

    @Column(name = "record_type")
    private int recordType;

    @Column(name = "record_id")
    private Long recordId;
}
