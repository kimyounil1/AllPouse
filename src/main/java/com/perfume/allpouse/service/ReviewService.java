package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.service.dto.SaveReviewDto;

import java.util.List;

public interface ReviewService {

    Long save(SaveReviewDto saveReviewDto);

    Long update(SaveReviewDto saveReviewDto);

    void delete(Long id);

    List<ReviewBoard> findByUserId(Long id);

    List<ReviewBoard> findByPerfumeId(Long id);
}
