package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
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
import springfox.documentation.annotations.ApiIgnore;

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

    private final BoardLogService boardLogService;


    // 향수 페이지
    @GetMapping("perfume/{perfumeId}")
    @Operation(summary="향수정보 및 리뷰 페이지", description="향수 상세 페이지. 기본 정보 및 리뷰 제공.")
    public SingleResponse<BestReviewDto> getPerfumePage(
            HttpServletRequest request,
            @ApiParam(value="향수 id", required=true) @PathVariable Long perfumeId,
            @ApiIgnore @PageableDefault(page=0, size=5, sort="hitCnt", direction=Sort.Direction.DESC) Pageable pageable) {
        final int size = 5;

        perfumeService.addHitCnt(perfumeId);

        long id = tokenProvider.getId(tokenProvider.resolveToken(request));


        PerfumeInfoDto perfumeInfo = perfumeService.getPerfumeInfo(perfumeId);

        List<ReviewResponseDto> perfumerReviews = reviewService.getReviewsOnPerfume(perfumeId, ROLE_PERFUMER, size);

        List<ReviewResponseDto> userReviews = reviewService.getReviewsOnPerfume(perfumeId, ROLE_USER, size);

        List<ReviewResponseDto> bestReviewsOnPerfume = reviewService.getReviewsOnPerfume(perfumeId, pageable);

        BestReviewDto bestReviewDto = new BestReviewDto(perfumeInfo, perfumerReviews, userReviews, bestReviewsOnPerfume);

        boardLogService.save(boardLogService.setSuccessLog(Thread.currentThread().getStackTrace()[1].getClassName(),Thread.currentThread().getStackTrace()[1].getMethodName(),
                "get single perfume detail",perfumeId,id));

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


    // 향수 10개
    @GetMapping("perfume")
    @Operation(summary = "(임시)향수 10개", description = "향수 10개 간단 정보 가져오는 API. 메인 페이지 향수 API 제작될 때까지만 사용")
    public ListResponse<PerfumeResponseDto> getPerfumeList() {

        final int size = 10;
        List<PerfumeResponseDto> perfumeDtoList = perfumeService.findAllWithSize(size);

        return responseService.getListResponse(perfumeDtoList);
    }


    // <요즘뜨는 향수> API
    // 조회수 순으로 향수 조회
    // 페이지네이션으로 page, size 설정가능
    @GetMapping(value = "rising-perfume")
    @Operation(summary = "요즘뜨는 향수", description = "요즘 뜨는 향수 가져오는 API. 페이지네이션으로 page, size 설정 가능." +
            "기본설정 : {page : 0, size : 16}")
    public PageResponse getRisingPerfume(
            @ApiParam(value = "페이지네이션 옵션", required = true)
            @PageableDefault(page = 0, size = 16) Pageable pageable) {

        Page<PerfumeResponseDto> pages = perfumeService.getPerfumeByHitCnt(pageable);

        return responseService.getPageResponse(pages);
    }



    // 특정 브랜드 향수 조회
    @GetMapping(value = "brand-perfume/{brandId}")
    @Operation(summary = "특정 브랜드 향수", description = "특정 브랜드 향수 가져오는 API.")
    public SingleResponse<PerfumeResponseSet> getPerfumesOnBrand(@ApiParam @PathVariable("brandId") Long brandId) {

        List<PerfumeResponseDto> perfumes = perfumeService.getPerfumeListByBrandId(brandId, 0);

        int size = perfumes.size();

        PerfumeResponseSet set = new PerfumeResponseSet(perfumes, size);

        return responseService.getSingleResponse(set);
    }




}
