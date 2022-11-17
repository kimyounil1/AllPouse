package com.perfume.allpouse.repository;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PerfumeBoardRepository extends JpaRepository<PerfumeBoard, Long> {
}
