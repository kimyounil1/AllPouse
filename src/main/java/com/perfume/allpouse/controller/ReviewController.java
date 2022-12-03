package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.*;
import com.perfume.allpouse.model.enums.BoardType;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.*;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.model.enums.Permission.*;

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
    @ResponseBody
    @PostMapping(value = "review", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public SingleResponse<Long> saveReview(
            HttpServletRequest request,
            @ApiParam(value = "리뷰 내용을 담는 DTO", required = false) @RequestPart SaveReviewDto saveReviewDto,
            @ApiParam(value = "리뷰에 첨부하는 사진들") @RequestPart(value = "photo", required = false) List<MultipartFile> photos) throws IOException {


        String token = tokenProvider.resolveToken(request);

        Long userId = tokenProvider.getId(token);

        Long reviewId = saveReviewDto.getId();


        // 첨부 사진 있는 경우
        if (photos != null) {

            // 첨부파일 저장
            photoService.delete(BoardType.REVIEW, reviewId);

            List<String> fileNameList = photoService.save(photos, BoardType.REVIEW, reviewId);

            if (reviewId == null) {
                saveReviewDto.setUserId(userId);
                Long savedId = reviewService.save(saveReviewDto);

                // response
                SingleResponse<Long> response = responseService.getSingleResponse(savedId);
                responseService.setSuccessResponse(response);

                return response;
            } else {
                Long savedId = reviewService.update(saveReviewDto);

                SingleResponse<Long> response = responseService.getSingleResponse(savedId);
                responseService.setSuccessResponse(response);

                return response;
            }
        }

        // 첨부 사진 없는 경우
        else {

            // 저장된 적 없는 리뷰 -> save
            if (reviewId == null) {
                saveReviewDto.setUserId(userId);
                Long savedId = reviewService.save(saveReviewDto);

                // response
                SingleResponse<Long> response = responseService.getSingleResponse(savedId);
                responseService.setSuccessResponse(response);

                return response;
            }
            // 저장된 적 있는 리뷰 -> update
            else {
                Long savedId = reviewService.update(saveReviewDto);

                // response
                SingleResponse<Long> response = responseService.getSingleResponse(savedId);
                responseService.setSuccessResponse(response);

                return response;
            }
        }
    }


    // 리뷰 삭제
    @ResponseBody
    @DeleteMapping("review")
    public CommonResponse deleteReview(
            HttpServletRequest request,
            @ApiParam(value = "삭제하려는 리뷰 id", required = true) @RequestParam Long reviewId) {

        String token = tokenProvider.resolveToken(request);
        Long userId = tokenProvider.getId(token);

        ReviewBoard review = reviewService.findById(reviewId);

        Long reviewUserId = review.getUser().getId();

        if (userId.equals(reviewUserId)) {
            reviewService.delete(reviewId);

            // response
            CommonResponse response = new CommonResponse();
            responseService.setSuccessResponse(response);
            return response;

        } else {
            throw new CustomException(INVALID_PARAMETER);
        }

    }


    // 회원이 쓴 리뷰 페이지 별로 가져옴
    // 쿼리파라미터로 페이지네이션 옵션 설정
    @ResponseBody
    @GetMapping("review/me")
    public Page<ReviewResponseDto> myReviewList(
            HttpServletRequest request,
            @ApiParam(value = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = Sort.Direction.DESC) Pageable pageable
            ) {

        String token = tokenProvider.resolveToken(request);

        Long userId = tokenProvider.getId(token);

        Page<ReviewResponseDto> pageList = reviewService.getReviewDto(userId, pageable);

        return pageList;
    }


    // 최근에 작성된 리뷰를 가져옴
    @ResponseBody
    @GetMapping("review/recent")
    @Operation(summary = "최근에 작성된 리뷰", description = "최근에 작성된 리뷰를 페이지별로 보여주는 API.")
    public Page<ReviewResponseDto> recentReview(
            @ApiParam(value = "페이지네이션 옵션", required = true)
            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        Page<ReviewResponseDto> pageList = reviewService.getRecentReviewDto(pageable);

        return pageList;
    }


    // 리뷰 상세 내용과 댓글 N개(N Default : 5)
    // 댓글은 최신순으로 가져옴
    @ResponseBody
    @GetMapping("review/{reviewId}")
    @Operation(summary = "리뷰/댓글(N개)", description = "리뷰와 해당 리뷰에 달린 댓글(N개)을 가져오는 API")
    public ReviewCommentDto getReviewPage(
            @ApiParam(value = "리뷰 id", required = true) @PathVariable("reviewId") Long reviewId) {

        final int size = 5;

        // 리뷰
        ReviewResponseDto reviewDto = reviewService.getReviewDtoByReviewId(reviewId);

        // 댓글
        <댓글 Service 계층 리팩토링>

        List<Comment> comments = commentService.findByReviewId(reviewId);
        int sizOfComments = Math.min(size, comments.size());
        List<Comment> slicedComments = comments.subList(0, sizOfComments);

        List<CommentResponseDto> commentDtoList = slicedComments.stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());

        ReviewCommentDto reviewCommentDto = new ReviewCommentDto(reviewDto, commentDtoList);

        return reviewCommentDto;
    }


    // 향수에 대한 review 분류별(조향사, 일반, 추천순)로 가져옴
    // 기본정렬 : 추천 내림차순(5개)
    @ResponseBody
    @GetMapping("review/best/{perfumeId}")
    @Operation(summary = "향수에 대한 분류별 리뷰", description = "향수에 대한 분류별 리뷰(조향사, 일반, 추천순) 가져옴")
    public BestReviewDto getBestReviews(
            @ApiParam(value = "향수 id", required = true) @PathVariable("perfumeId") Long perfumeId,
            @PageableDefault(page = 0, size = 5, sort = "hitCnt", direction = Sort.Direction.DESC) Pageable pageable) {

        PerfumeBoard perfume = perfumeService.findOne(perfumeId);
        PerfumeResponseDto perfumeDto = PerfumeResponseDto.toDto(perfume);

        List<ReviewResponseDto> perfumerReviews = reviewService.getReviewDtoByPerfumeIdAndPermission(perfumeId, ROLE_PERFUMER);

        List<ReviewResponseDto> userReviews = reviewService.getReviewDtoByPerfumeIdAndPermission(perfumeId, ROLE_USER);

        List<ReviewResponseDto> bestReviewsOnPerfume = reviewService.getReviewDtoByPerfumeId(perfumeId, pageable);

        return new BestReviewDto(perfumeDto, perfumerReviews, userReviews, bestReviewsOnPerfume);
    }
}



