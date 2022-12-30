package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.repository.custom.BrandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface BrandRepository extends JpaRepository<Brand, Long>, BrandRepositoryCustom {

    public List<Brand> findByNameContainingOrderByNameAsc(String name);
}
