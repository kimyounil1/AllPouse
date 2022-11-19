package com.perfume.allpouse.repository;

import com.perfume.allpouse.data.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    public List<Brand> findByNameContainingOrderByNameAsc(String name);



}
