package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.ReviewBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {

    @Query("select r from ReviewBoard r " +
            "where r.user.id = :userId " +
            "order by r.createDateTime asc")
    List<ReviewBoard> findReviewsByUserId(@Param("userId") Long id);


    @Query("select r from ReviewBoard r " +
            "where r.perfume.id = :perfumeId " +
            "order by r.createDateTime")
    List<ReviewBoard> findReviewsByPerfumeId(@Param("perfumeId") Long id);
}

