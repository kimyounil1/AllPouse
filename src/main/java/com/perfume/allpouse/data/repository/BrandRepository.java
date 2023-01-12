package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.repository.custom.BrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BrandRepository extends JpaRepository<Brand, Long>, BrandRepositoryCustom {

    List<Brand> findByNameContainingOrderByNameAsc(String name);

    // 조회수 +1
    @Modifying
    @Query("update Brand b set b.hitCnt = b.hitCnt + 1 where b.id = :brandId")
    void updateHitCnt(@Param("brandId") Long id);

}
