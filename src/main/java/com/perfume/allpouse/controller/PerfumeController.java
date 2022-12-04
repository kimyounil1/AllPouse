package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.model.dto.BestReviewDto;
import com.perfume.allpouse.model.dto.PerfumeInfoDto;
import com.perfume.allpouse.model.dto.PerfumePageDto;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.PerfumeService;
import com.perfume.allpouse.service.ResponseService;
import com.perfume.allpouse.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.perfume.allpouse.model.enums.Permission.ROLE_PERFUMER;
import static com.perfume.allpouse.model.enums.Permission.ROLE_USER;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class PerfumeController {

    private final Logger LOGGER = LoggerFactory.getLogger(PerfumeController.class);

    private final TokenProvider tokenProvider;

    private final PerfumeService perfumeService;

    private final ResponseService responseService;

    private final ReviewService reviewService;


    // 향수 페이지
    @ResponseBody
    @Operation(summary = "향수정보 및 리뷰 페이지", description = "향수 상세 페이지. 기본 정보 및 리뷰 제공. 페이지네이션 파라미터 전달하지 않아도 됨.")
    @GetMapping("perfume/{perfumeId}")
    public SingleResponse<BestReviewDto> getPerfumePage(@ApiParam(value = "향수 id", required = true) @PathVariable Long perfumeId,
                                                         @PageableDefault(page = 0, size = 5, sort = "hitCnt", direction = Sort.Direction.DESC) Pageable pageable) {

        final int size = 5;

        PerfumeInfoDto perfumeInfo = perfumeService.getPerfumeInfo(perfumeId);

        List<ReviewResponseDto> perfumerReviews = reviewService.getReviewDtoByPerfumeIdAndPermission(perfumeId, ROLE_PERFUMER, size);

        List<ReviewResponseDto> userReviews = reviewService.getReviewDtoByPerfumeIdAndPermission(perfumeId, ROLE_USER, size);

        List<ReviewResponseDto> bestReviewsOnPerfume = reviewService.getReviewDtoByPerfumeId(perfumeId, pageable);

        BestReviewDto bestReviewDto = new BestReviewDto(perfumeInfo, perfumerReviews, userReviews, bestReviewsOnPerfume);

        return responseService.getSingleResponse(bestReviewDto);

    }

    // 향수 등록 및 수정


    // 향수 삭제



}
