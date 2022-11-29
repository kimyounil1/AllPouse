package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c " +
            "where c.user.id = :userId " +
            "order by c.createDateTime asc")
    List<Comment> findCommentsByUserId(@Param("userId") Long id);


    @Query("select c from Comment c " +
            "where c.review.id = :reviewId " +
            "order by c.createDateTime asc")
    List<Comment> findCommentsByReviewId(@Param("reviewId") Long id);

    Page<Comment> findCommentsByUserId(Long userId, Pageable pageable);
}
