package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.PostComment;
import com.perfume.allpouse.data.repository.custom.PostCommentRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentRepository extends JpaRepository<PostComment, Long>, PostCommentRepositoryCustom {


    // 추천수 + 1
    @Modifying
    @Query("update PostComment pc set pc.recommendCnt = pc.recommendCnt + 1 where pc.id = :commentId")
    void addRecommendCnt(@Param("commentId") Long commentId);

    // 추천수 - 1
    @Modifying
    @Query("update PostComment pc set pc.recommendCnt = pc.recommendCnt - 1 where pc.id = :commentId")
    void minusRecommendCnt(@Param("commentId") Long commentId);



}
