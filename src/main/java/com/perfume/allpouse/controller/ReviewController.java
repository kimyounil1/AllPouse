package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.*;
import com.perfume.allpouse.model.reponse.CommonResponse;
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
import static com.perfume.allpouse.model.enums.BoardType.*;
import static com.perfume.allpouse.model.enums.Permission.ROLE_PERFUMER;
import static com.perfume.allpouse.model.enums.Permission.ROLE_USER;

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
    public CommonResponse saveAndUpdateReview(
            HttpServletRequest request,
            @ApiParam(value = "리뷰 내용을 담는 DTO", required = false) @RequestPart SaveReviewDto saveReviewDto,
            @ApiParam(value = "리뷰에 첨부하는 사진들") @RequestPart(value = "photo", required = false) List<MultipartFile> photos) throws IOException {

        Long userId = getUserIdFromRequest(request);

        // 첨부 사진 있는 경우
        if (photos != null) {
            reviewService.save(saveReviewDto, photos);
        }
        // 첨부 사진 없는 경우
        else {
            reviewService.save(saveReviewDto);
        }
        return responseService.getSuccessCommonResponse();
    }


    // 리뷰 삭제
    @DeleteMapping("review")
    @Operation(summary = "리뷰 삭제", description = "reviewId 받아서 해당 리뷰 삭제하는 API")
    public CommonResponse deleteReview(
            HttpServletRequest request,
            @ApiParam(value = "삭제하려는 리뷰 id", required = true) @RequestParam Long reviewId) {

        Long userId = getUserIdFromRequest(request);

        ReviewBoard review = reviewService.findById(reviewId);

        Long reviewUserId = review.getUser().getId();

        // reviewId로 찾은 리뷰 작성자와 토큰으로 확인한 유저가 동일할 때만 삭제 실행
        if (userId.equals(reviewUserId)) {
            reviewService.delete(reviewId);
            photoService.delete(REVIEW, reviewId);
            return responseService.getSuccessCommonResponse();
        } else {throw new CustomException(INVALID_PARAMETER);}
    }


    // 회원이 작성한 리뷰
    @GetMapping("review/me")
    @Operation(summary = "회원이 쓴 리뷰", description = "회원이 작성한 리뷰 가져오는 API. 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음")
    public PageResponse myReviewList(
            HttpServletRequest request,
            @ApiParam(value = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = Sort.Direction.DESC) Pageable pageable
            ) {

        Long userId = getUserIdFromRequest(request);

        Page<ReviewResponseDto> pages = reviewService.getReviewDto(userId, pageable);

        return responseService.getPageResponse(pages);
    }


    // 최근에 작성된 리뷰
    @GetMapping("review/recent")
    @Operation(summary = "최근에 작성된 리뷰", description = "최근에 작성된 리뷰를 페이지별로 보여주는 API.")
    public PageResponse recentReview(
            @ApiParam(value = "페이지네이션 옵션", required = true)
            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<ReviewResponseDto> pages = reviewService.getRecentReviewDto(pageable);

        return responseService.getPageResponse(pages);
    }


    // 리뷰 상세 내용과 댓글 N개(N Default : 5)
    // 정렬 : 최신순(고정)
    @GetMapping("review/{reviewId}")
    @Operation(summary = "리뷰/댓글(N개)", description = "리뷰와 해당 리뷰에 달린 댓글(N개)을 가져오는 API")
    public SingleResponse<ReviewCommentDto> getReviewPage(
            @ApiParam(value = "리뷰 id", required = true) @PathVariable("reviewId") Long reviewId) {

        final int size = 5;

        reviewService.addHitCnt(reviewId);

        // 리뷰
        ReviewResponseDto reviewDto = reviewService.getReviewDtoByReviewId(reviewId);

        // 댓글
        List<Comment> comments = commentService.findByReviewId(reviewId, size);
        List<CommentResponseDto> commentDtoList = CommentResponseDto.toDtoList(comments);

        ReviewCommentDto reviewCommentDto = new ReviewCommentDto(reviewDto, commentDtoList);

        return responseService.getSingleResponse(reviewCommentDto);
    }


    // 향수에 대한 review 분류별(조향사, 일반, 추천순)로 가져옴
    // 기본정렬 : 추천 내림차순(5개)
    @GetMapping("review/best/{perfumeId}")
    @Operation(summary = "향수에 대한 분류별 리뷰", description = "향수에 대한 분류별 리뷰(조향사, 일반, 추천순) 가져옴")
    public SingleResponse<BestReviewDto> getBestReviews(
            @ApiParam(value = "향수 id", required = true) @PathVariable("perfumeId") Long perfumeId,
            @PageableDefault(page = 0, size = 5, sort = "hitCnt", direction = Sort.Direction.DESC) Pageable pageable) {

        final int size = 5;

        PerfumeInfoDto perfumeInfo = perfumeService.getPerfumeInfo(perfumeId);

        List<ReviewResponseDto> perfumerReviews = reviewService.getReviewsOnPerfume(perfumeId, ROLE_PERFUMER, size);

        List<ReviewResponseDto> userReviews = reviewService.getReviewsOnPerfume(perfumeId, ROLE_USER, size);

        List<ReviewResponseDto> bestReviewsOnPerfume = reviewService.getReviewsOnPerfume(perfumeId, pageable);

        BestReviewDto bestReviewDto = new BestReviewDto(perfumeInfo, perfumerReviews, userReviews, bestReviewsOnPerfume);


        return responseService.getSingleResponse(bestReviewDto);
    }


    // 리뷰 추천
    @GetMapping("review/recommend/{reviewId}")


    // HttpRequest에서 userId 추출
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        return tokenProvider.getId(token);
    }
}



