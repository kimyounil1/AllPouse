package com.perfume.allpouse.repository;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerfumeBoardRepository extends JpaRepository<PerfumeBoard, Long> {

    @Query("select p from PerfumeBoard p where p.brand.id = :brandId order by p.subject asc")
    List<PerfumeBoard> findByBrandId(@Param("brandId") Long id);

}
