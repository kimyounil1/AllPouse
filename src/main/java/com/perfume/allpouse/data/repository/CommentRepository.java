package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.data.entity.QComment;
import com.perfume.allpouse.data.repository.custom.CommentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @Query("select c from Comment c " +
            "where c.user.id = :userId " +
            "order by c.createDateTime desc")
    List<Comment> findCommentsByUserId(@Param("userId") Long id);


    @Query("select c from Comment c " +
            "where c.review.id = :reviewId " +
            "order by c.createDateTime desc")
    List<Comment> findCommentsByReviewId(@Param("reviewId") Long id);

    Page<Comment> findCommentsByUserId(Long userId, Pageable pageable);

    Page<Comment> findAll(Pageable pageable);

    Page<Comment> findCommentsByReviewId(Long reviewId, Pageable pageable);




}
