package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.model.enums.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {

    Long save(SaveReviewDto saveReviewDto);

    Long update(SaveReviewDto saveReviewDto);

    void delete(Long id);

    List<ReviewBoard> findByUserId(Long id);

    List<ReviewBoard> findByPerfumeId(Long id);

    ReviewBoard findById(Long id);

    Page<ReviewResponseDto> getReviewDto(Long userId, Pageable pageable);

    Page<ReviewResponseDto> getRecentReviewDto(Pageable pageable);

    ReviewResponseDto getReviewDtoByReviewId(Long reviewId);

    List<ReviewResponseDto> getReviewDtoByPerfumeId(Long perfumeId, Pageable pageable);

    List<ReviewResponseDto> getReviewDtoByPerfumeIdAndPermission(Long perfumeId, Permission permission);
}
