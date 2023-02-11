package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.*;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.ListResponse;
import com.perfume.allpouse.model.reponse.PageResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.*;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.model.enums.BoardType.REVIEW;
import static com.perfume.allpouse.model.enums.Permission.ROLE_PERFUMER;
import static com.perfume.allpouse.model.enums.Permission.ROLE_USER;

/**
 * [리뷰 컨트롤러]
 *
 * 1> API 메소드 목록
 * - saveAndUpdateReview : 리뷰 저장 및 업데이트
 * - deleteReview : 리뷰 삭제
 * - getMyReviewList : 회원이 작성한 리뷰
 * - recentReview : 최근에 작성한 리뷰
 * - getReviewPage : 리뷰 상세 내용과 댓글 N개
 * - getBestReview : (특정 향수에 대한) 조향사, 일반, 추천순 리뷰
 * - recommendReview : 리뷰 추천
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/")
public class ReviewController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private final TokenProvider tokenProvider;

    private final ReviewService reviewService;

    private final ResponseService responseService;

    private final PhotoService photoService;

    private final CommentService commentService;

    private final PerfumeService perfumeService;


    // 리뷰 저장 및 업데이트
    @PostMapping(value = "review", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "리뷰 저장 및 수정", description = "리뷰를 저장하거나 수정하는 API")
    public SingleResponse<Long> saveAndUpdateReview(
            HttpServletRequest request,
            @ApiParam(value = "리뷰 내용을 담는 DTO", required = true) @RequestPart SaveReviewDto saveReviewDto,
            @ApiParam(value = "리뷰에 첨부하는 사진들") @RequestPart(value = "photo", required = false) List<MultipartFile> photos) throws IOException {

        Long userId = tokenProvider.getUserIdFromRequest(request);
        saveReviewDto.setUserId(userId);

        // 첨부 사진 있는 경우
        if (photos != null) {
            Long savedId = reviewService.save(saveReviewDto, photos);
            return responseService.getSingleResponse(savedId);
        }
        // 첨부 사진 없는 경우
        else {
            Long savedId = reviewService.save(saveReviewDto);
            return responseService.getSingleResponse(savedId);

        }
    }


    // // 리뷰 업데이트
    //@PatchMapping(value = "review", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    //@Operation(summary = "리뷰 업데이트", description = "리뷰 수정하는 API")
    //public SingleResponse<Long> updateReview(
    //        HttpServletRequest request,
    //        @ApiParam(value = "업데이트할 리뷰 내용 담는 DTO", required = true) @RequestPart SaveReviewDto saveReviewDto,
    //        @ApiParam(value = "리뷰에 첨부할 사진들") @RequestPart(value = "photo", required = false) List<MultipartFile> photos) throws IOException {
//
//
    //    // 첨부 사진 있는 경우
    //    if (photos != null) {
    //        Long savedId = reviewService.save(saveReviewDto, photos);
    //        return responseService.getSingleResponse(savedId);
    //    }
    //    // 첨부 사진 없는 경우
    //    else {
    //        Long savedId = reviewService.save(saveReviewDto);
    //        return responseService.getSingleResponse(savedId);
    //    }
    //}


    // 리뷰 삭제
    @DeleteMapping(value = "review")
    @Operation(summary = "리뷰 삭제", description = "reviewId 받아서 해당 리뷰 삭제하는 API")
    public CommonResponse deleteReview(
            HttpServletRequest request,
            @ApiParam(value = "삭제하려는 리뷰 id", required = true) @RequestParam Long reviewId) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        ReviewBoard review = reviewService.findById(reviewId);

        Long reviewUserId = review.getUser().getId();

        // reviewId로 찾은 리뷰 작성자와 토큰으로 확인한 유저가 동일할 때만 삭제 실행
        if (userId.equals(reviewUserId)) {
            reviewService.delete(reviewId);
            photoService.delete(REVIEW, reviewId);
            return responseService.getSuccessCommonResponse();
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
    }


    // 회원이 작성한 리뷰
    // 쿼리 파라미터(pageable에 매핑됨)로 페이지네이션 옵션 설정
    // 기본 설정 : 20 posts per Page, 최신순 정렬
    @GetMapping(value="review/user/{period}}")
    @Operation(summary="회원이 쓴 리뷰(기간별)",
            description = "회원이 작성한 리뷰 가져오는 API. period 파라미터로 가져올 기간 설정할 수 있음. " +
                    "<기간 설정> : { 0: 전체, 1: 1주, 2: 1개월, 3: 6개월 }")
    public ListResponse<ReviewResponseDto> getMyReviewList(
            HttpServletRequest request,
            @ApiParam(value="리뷰 가져올 기간 설정", required=true) @RequestParam("period") int periodNum) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        List<ReviewResponseDto> reviewDtoList = reviewService.getReviewsByPeriod(userId, periodNum);

        return responseService.getListResponse(reviewDtoList);
    }


    // 최근에 작성된 리뷰
    @GetMapping(value = "review/recent")
    @Operation(summary = "최근에 작성된 리뷰", description = "최근에 작성된 리뷰를 페이지별로 보여주는 API.")
    public PageResponse recentReview(
            HttpServletRequest request,
            @ApiParam(value="페이지네이션 옵션", required = true)
            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        Page<ReviewResponseDto> pages = reviewService.getRecentReviewDto(pageable);

        // 유저가 리뷰 추천했는지 여부 넣어줌
        pages.forEach(
                dto -> dto.setRecommended(reviewService.isRecommended(dto.getId(), userId))
        );

        return responseService.getPageResponse(pages);
    }


    // 리뷰 상세 내용과 댓글 N개(N Default : 5)
    // 정렬 : 최신순(고정)
    @GetMapping(value = "review/{reviewId}")
    @Operation(summary = "리뷰/댓글(N개)", description = "리뷰와 해당 리뷰에 달린 댓글(N개)을 가져오는 API")
    public SingleResponse<ReviewCommentDto> getReviewPage(
            HttpServletRequest request,
            @ApiParam(value = "리뷰 id", required = true) @PathVariable("reviewId") Long reviewId) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        final int size = 5;
        // 리뷰
        ReviewResponseDto reviewDto = reviewService.getReviewDtoByReviewId(reviewId);

        // 유저가 리뷰 추천했는지 여부 넣어줌
        reviewDto.setRecommended(reviewService.isRecommended(reviewDto.getId(), userId));

        // 댓글
        List<CommentResponseDto> commentDtoList = commentService.findByReviewId(reviewId, size);

        ReviewCommentDto reviewCommentDto = new ReviewCommentDto(reviewDto, commentDtoList);

        return responseService.getSingleResponse(reviewCommentDto);
    }


    // 향수에 대한 review 분류별(조향사, 일반, 추천순)로 가져옴
    // 기본정렬 : 추천 내림차순(5개)
    @GetMapping(value = "review/best/{perfumeId}")
    @Operation(summary = "향수에 대한 분류별 리뷰", description = "향수에 대한 분류별 리뷰(조향사, 일반, 추천순) 가져옴")
    public SingleResponse<BestReviewDto> getBestReviews(
            HttpServletRequest request,
            @ApiParam(value = "향수 id", required = true) @PathVariable("perfumeId") Long perfumeId,
            @PageableDefault(page = 0, size = 5, sort = "hitCnt", direction = Sort.Direction.DESC) Pageable pageable) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        final int size = 5;

        PerfumeInfoDto perfumeInfo = perfumeService.getPerfumeInfo(perfumeId);


        // 조향사 리뷰들
        List<ReviewResponseDto> perfumerReviews = reviewService.getReviewsOnPerfume(perfumeId, ROLE_PERFUMER, size);

        perfumerReviews.forEach(dto -> dto.setRecommended(reviewService.isRecommended(dto.getId(), userId)));


        // 일반 사용자 리뷰들
        List<ReviewResponseDto> userReviews = reviewService.getReviewsOnPerfume(perfumeId, ROLE_USER, size);

        userReviews.forEach(dto -> dto.setRecommended(reviewService.isRecommended(dto.getId(), userId)));


        // 베스트 리뷰들
        List<ReviewResponseDto> bestReviewsOnPerfume = reviewService.getReviewsOnPerfume(perfumeId, pageable);

        bestReviewsOnPerfume.forEach(dto -> dto.setRecommended(reviewService.isRecommended(dto.getId(), userId)));



        BestReviewDto bestReviewDto = new BestReviewDto(perfumeInfo, perfumerReviews, userReviews, bestReviewsOnPerfume);


        return responseService.getSingleResponse(bestReviewDto);
    }


    // 향수리뷰 추천 API
    @PostMapping("review/recommend/{reviewId}")
    @Operation(summary = "향수 리뷰 추천 API", description = "향수 리뷰 추천 API. " +
            "API 호출한 시점 기준으로 만약 이전에 추천한 적 없다면 추천하기 실행되고, 추천한 적있다면 추천 취소 실행")
    public CommonResponse recommendReview(
            HttpServletRequest request,
            @ApiParam(value = "향수리뷰 id", required = true) @PathVariable("reviewId")  Long reviewId) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        int index = reviewService.updateRecommendCnt(reviewId, userId);

        // 추천
        if (index == 0) {
            return new CommonResponse(true, 0, "추천완료");
        }
        // 추천취소
        else {
            return new CommonResponse(true, 0, "추천취소완료");
        }
    }

}



