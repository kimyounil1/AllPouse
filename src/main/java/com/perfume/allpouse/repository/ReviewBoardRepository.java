package com.perfume.allpouse.repository;

import com.perfume.allpouse.data.entity.ReviewBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {

}

