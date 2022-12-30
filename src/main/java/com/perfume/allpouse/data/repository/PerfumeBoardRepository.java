package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.repository.custom.PerfumeBoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerfumeBoardRepository extends JpaRepository<PerfumeBoard, Long>, PerfumeBoardRepositoryCustom {

    @Query("select p from PerfumeBoard p where p.brand.id = :brandId order by p.subject asc")
    List<PerfumeBoard> findByBrandId(@Param("brandId") Long id);


    @Modifying
    @Query("update PerfumeBoard p set p.hitCnt = p.hitCnt + 1 where p.id = :perfumeId")
    void updateHitCnt(@Param("perfumeId") Long id);

}
