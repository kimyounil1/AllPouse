package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.repository.custom.ReviewBoardRepositoryCustom;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.enums.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long>, ReviewBoardRepositoryCustom {


    @Query("select r from ReviewBoard r " +
            "where r.user.id = :userId " +
            "order by r.createDateTime desc")
    List<ReviewBoard> findReviewsByUserId(@Param("userId") Long id);

    @Query("select r from ReviewBoard r " +
            "where r.perfume.id = :perfumeId " +
            "order by r.createDateTime")
    List<ReviewBoard> findReviewsByPerfumeId(@Param("perfumeId") Long id);

    @Query("select r from ReviewBoard r " +
            "where r.perfume.id = :perfumeId " +
            "and r.user.permission = :permission " +
            "order by r.recommendCnt desc")
    List<ReviewBoard> findReviewBoardByPerfumeIdAndUserPermission(@Param("perfumeId") Long perfumeId,
                                                                  @Param("permission")Permission permission);


    // 조회수 +1
    @Modifying
    @Query("update ReviewBoard r set r.hitCnt = r.hitCnt + 1 where r.id = :reviewId")
    void updateHitCnt(@Param("reviewId") Long reviewId);

    // 추천수 +1
    @Modifying
    @Query("update ReviewBoard r set r.recommendCnt = r.recommendCnt + 1 where r.id = :reviewId")
    void addRecommendCnt(@Param("reviewId") Long reviewId);

    // 추천수 -1
    @Modifying
    @Query("update ReviewBoard r set r.recommendCnt = r.recommendCnt - 1 where r.id = :reviewId")
    void minusRecommendCnt(@Param("reviewId") Long reviewId);

}

