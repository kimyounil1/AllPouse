package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Query("update Post p set p.hitCnt = p.hitCnt + 1 where p.user.id = :userId")
    void updateHitCnt(@Param("userId") Long id);

}
