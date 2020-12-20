package com.blog.template.dao;

import com.blog.template.models.likerecord.LikeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LikeRecordDao extends JpaRepository<LikeRecord,Long>, JpaSpecificationExecutor<LikeRecord> {

    List<LikeRecord> findByRecordTypeAndUserIdAndRecordIdIn(int recordType,Long userId,List<Long> recordIdList);

    Optional<LikeRecord> findByRecordTypeAndUserIdAndRecordId(int recordType,Long userId,Long recordId);
}
