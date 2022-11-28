package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.PhotoDto;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.model.enums.BoardType;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.ListResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.ResponseService;
import com.perfume.allpouse.service.ReviewService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;

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

    private final EntityManager em;


    // 리뷰 저장 및 업데이트
    @ResponseBody
    @PostMapping(value = "review", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public SingleResponse<Long> saveReview(
            HttpServletRequest request,
            @ApiParam(value = "리뷰 내용을 담는 DTO") @RequestPart SaveReviewDto saveReviewDto,
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
    // pageable : offset, pageNumber, pageSize, sort
    @ResponseBody
    @GetMapping("review/me")
    public Page<ReviewResponseDto> myReviewList(HttpServletRequest request,
                                                @ApiParam(value = "페이지네이션 옵션", required = true) Pageable pageable
    ) {

        String token = tokenProvider.resolveToken(request);

        Long userId = tokenProvider.getId(token);

        List<ReviewResponseDto> reviewDtoList = reviewService.getReviewDto(userId);

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewDtoList.size());
        Page<ReviewResponseDto> page = new PageImpl<>(reviewDtoList.subList(start, end), pageable, reviewDtoList.size());

        return page;
    }

}



