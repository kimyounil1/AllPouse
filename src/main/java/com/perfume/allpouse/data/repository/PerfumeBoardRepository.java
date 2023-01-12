package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.repository.custom.PerfumeBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PerfumeBoardRepository extends JpaRepository<PerfumeBoard, Long>, PerfumeBoardRepositoryCustom {

    // 조회수 +1
    @Modifying
    @Query("update PerfumeBoard p set p.hitCnt = p.hitCnt + 1 where p.id = :perfumeId")
    void updateHitCnt(@Param("perfumeId") Long id);



}
