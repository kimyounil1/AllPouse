package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Page<PostComment> findPostCommentsByUserId(Long userId, Pageable pageable);

    // 추천 + 1
    @Modifying
    @Query("update PostComment pc set pc.recommendCnt = pc.recommendCnt + 1 where pc.user.id = :userId")
    void addRecommendCnt(@Param("userId") Long id);

    // 추천 - 1
    @Modifying
    @Query("update PostComment pc set pc.recommendCnt = pc.recommendCnt - 1 where pc.user.id = :userId")
    void minusRecommendCnt(@Param("userId") Long id);



}
