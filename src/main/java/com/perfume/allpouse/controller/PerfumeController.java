package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.model.dto.BestReviewDto;
import com.perfume.allpouse.model.dto.PerfumeInfoDto;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.PerfumeService;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.ResponseService;
import com.perfume.allpouse.service.ReviewService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.*;
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

    private final ReviewService reviewService;

    private final PhotoService photoService;

    private final ResponseService responseService;


    // 향수 페이지
    @GetMapping("perfume/{perfumeId}")
    @Operation(summary = "향수정보 및 리뷰 페이지", description = "향수 상세 페이지. 기본 정보 및 리뷰 제공. (페이지네이션 파라미터 전달하지 않아도 됨)")
    public SingleResponse<BestReviewDto> getPerfumePage(@ApiParam(value = "향수 id", required = true) @PathVariable Long perfumeId,
                                                        @PageableDefault(page = 0, size = 5, sort = "hitCnt", direction = Sort.Direction.DESC) Pageable pageable) {
        final int size = 5;

        perfumeService.addHitCnt(perfumeId);

        PerfumeInfoDto perfumeInfo = perfumeService.getPerfumeInfo(perfumeId);

        List<ReviewResponseDto> perfumerReviews = reviewService.getReviewsOnPerfume(perfumeId, ROLE_PERFUMER, size);

        List<ReviewResponseDto> userReviews = reviewService.getReviewsOnPerfume(perfumeId, ROLE_USER, size);

        List<ReviewResponseDto> bestReviewsOnPerfume = reviewService.getReviewsOnPerfume(perfumeId, pageable);

        BestReviewDto bestReviewDto = new BestReviewDto(perfumeInfo, perfumerReviews, userReviews, bestReviewsOnPerfume);

        return responseService.getSingleResponse(bestReviewDto);

    }

    // 향수 등록 및 수정
    @PostMapping(value = "perfume", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "<사용X> 향수 저장 및 수정", description = "*** <데이터 추가용> 향수를 저장하거나 수정하는 API")
    public CommonResponse saveAndUpdatePerfume(
            @ApiParam(value = "향수 내용을 담는 DTO", required = true) @RequestPart SavePerfumeDto savePerfumeDto,
            @ApiParam(value = "향수 사진", required = false) @RequestPart(value = "photo", required = false) List<MultipartFile> photos) throws IOException {

        // 첨부사진 있는 경우
        if (photos != null) {
            perfumeService.save(savePerfumeDto, photos);
        }
        // 첨부사진 없는 경우
        else {
            perfumeService.save(savePerfumeDto);
        }
        return responseService.getSuccessCommonResponse();
    }


    // 향수 삭제
    @DeleteMapping("perfume")
    @Operation(summary = "향수 삭제", description = "perfumeId 받아서 해당 향수 삭제하는 API")
    public CommonResponse deletePerfume(@ApiParam(value = "삭제하려는 향수 id", required = true) @RequestParam Long perfumeId) {

        perfumeService.delete(perfumeId);

        photoService.delete(PERFUME, perfumeId);

        return responseService.getSuccessCommonResponse();
    }


    // HttpRequest에서 userId 추출
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        return tokenProvider.getId(token);
    }



}
