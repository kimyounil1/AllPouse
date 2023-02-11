package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SearchReviewDto;
import com.perfume.allpouse.model.enums.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewBoardRepositoryCustom {

    // 기본검색
    List<ReviewResponseDto> search(String keyword);

    // 기본검색 -> 전체 리뷰
    Page<ReviewResponseDto> searchWithPaging(String keyword, Pageable pageable);

    // 유저작성 리뷰(+ 페이지네이션)
    Page<ReviewResponseDto> getReviewDto(Long userId, Pageable pageable);

    // 전체 리뷰 DTO
    Page<ReviewResponseDto> getRecentReviewDto(Pageable pageable);

    // 리뷰 id로 리뷰 검색해서 ReviewResponseDto 가져옴
    ReviewResponseDto getReviewDtoByReviewId(Long reviewId);

    // 브랜드에 달린 리뷰 DTO
    List<ReviewResponseDto> getReviewsOnBrand(Long boardId, Pageable pageable);

    // 브랜드에 달린 사용자분류(USER : 일반사용자, PERFUMER : 조향사)별 베스트 리뷰 DTO
    List<ReviewResponseDto> getReviewsOnBrand(Long boardId, Permission permission, int size);

    // 향수에 달린 리뷰 DTO
    List<ReviewResponseDto> getReviewsOnPerfume(Long perfumeId, Pageable pageable);

    // 향수에 달린 사용자분류(USER : 일반사용자, PERFUMER : 조향사)별 베스트 리뷰 DTO
    List<ReviewResponseDto> getReviewsOnPerfume(Long perfumeId, Permission permission, int size);

    // 회원이 작성한 리뷰 기간 별로 가져옴
    List<ReviewResponseDto> getReviewsByPeriod(Long userId, int periodNum);


}
