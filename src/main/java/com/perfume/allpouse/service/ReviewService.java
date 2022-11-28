package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {

    Long save(SaveReviewDto saveReviewDto);

    Long update(SaveReviewDto saveReviewDto);

    void delete(Long id);

    List<ReviewBoard> findByUserId(Long id);

    List<ReviewBoard> findByPerfumeId(Long id);

    ReviewBoard findById(Long id);

    List<ReviewResponseDto> getReviewDto(Long userId);
}
