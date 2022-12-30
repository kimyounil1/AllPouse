package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.data.repository.custom.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    // 조회수 +1
    @Modifying
    @Query("update Post p set p.hitCnt = p.hitCnt + 1 where p.id = :postId")
    void updateHitCnt(@Param("postId") Long postId);

    // 추천수 +1
    @Modifying
    @Query("update Post p set p.recommendCnt = p.recommendCnt + 1 where p.id =:postId")
    void addRecommendCnt(@Param("postId") Long postId);

    // 추천수 -1
    @Modifying
    @Query("update Post p set p.recommendCnt = p.recommendCnt - 1 where p.id =:postId")
    void minusRecommendCnt(@Param("postId") Long postId);
}
