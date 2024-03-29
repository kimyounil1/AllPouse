package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.model.enums.BoardType;
import com.perfume.allpouse.model.enums.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReviewService {

    // 저장, 업데이트, 삭제
    Long save(SaveReviewDto saveReviewDto);

    Long save(SaveReviewDto saveReviewDto, List<MultipartFile> photos) throws IOException;

    Long update(SaveReviewDto saveReviewDto);

    void delete(Long ReviewId);



    // 조회
    List<ReviewBoard> findByUserId(Long id);

    List<ReviewBoard> findByPerfumeId(Long id);

    ReviewBoard findById(Long id);

    Page<ReviewResponseDto> getReviewDto(Long userId, Pageable pageable);

    Page<ReviewResponseDto> getRecentReviewDto(Pageable pageable);

    ReviewResponseDto getReviewDtoByReviewId(Long reviewId);

    List<ReviewResponseDto> getReviewsOnBrand(Long boardId, Pageable pageable);

    List<ReviewResponseDto> getReviewsOnBrand(Long boardId, Permission permission, int size);

    List<ReviewResponseDto> getReviewsOnPerfume(Long boardId, Pageable pageable);

    List<ReviewResponseDto> getReviewsOnPerfume(Long boardId, Permission permission, int size);

    List<ReviewResponseDto> getReviewsByPeriod(Long userId, int periodNum);

    // 기타 로직
    boolean isRecommended(Long reviewId, Long userId);

    int updateRecommendCnt(Long reviewId, Long userId);
}
