package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.ResponseService;
import com.perfume.allpouse.service.ReviewService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {


    private final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private final TokenProvider tokenProvider;

    private final ReviewService reviewService;

    private final ResponseService responseService;


    @ResponseBody
    @PostMapping
    public SingleResponse<Long> saveReview(
            HttpServletRequest request,
            @ApiParam(value = "saveReviewDto", required = true) @RequestBody SaveReviewDto saveReviewDto) {

        String token = tokenProvider.resolveToken(request);

        Long userId = tokenProvider.getId(token);

        Long reviewId = saveReviewDto.getId();

        if (reviewId == null) {
            saveReviewDto.setUserId(Long.valueOf(userId));
            Long savedId = reviewService.save(saveReviewDto);

            //response
            SingleResponse<Long> response = responseService.getSingleResponse(savedId);
            responseService.setSuccessResponse(response);
            return response;
        }

        else {
            Long savedId = reviewService.update(saveReviewDto);

            //response
            SingleResponse<Long> response = responseService.getSingleResponse(savedId);
            responseService.setSuccessResponse(response);
            return response;
        }
    }


    @ResponseBody
    @DeleteMapping
    public CommonResponse deleteReview(
            HttpServletRequest request,
            @ApiParam(value = "reviewId", required = true) @RequestParam Long reviewId) {

        String token = tokenProvider.resolveToken(request);

        Long userId = tokenProvider.getId(token);

        ReviewBoard review = reviewService.findById(reviewId);

        Long reviewUserId = review.getUser().getId();

        if (userId.equals(reviewUserId)) {
            reviewService.delete(reviewId);

            //response
            CommonResponse response = new CommonResponse();
            responseService.setSuccessResponse(response);

            return response;
        }

        else {
            //response
            CommonResponse response = new CommonResponse();
            response.setCode(400);
            response.setSuccess(false);
            response.setMsg("파라미터 값을 확인해주세요.");

            return response;
        }


    }



    // review 삭제

    // review 조회 -> UserController에서
}
