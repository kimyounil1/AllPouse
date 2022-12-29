package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface SearchBrandRepository extends JpaRepository<Brand, Long>, com.perfume.allpouse.data.repository.search.SearchBrandRepository {

    public List<Brand> findByNameContainingOrderByNameAsc(String name);



}
