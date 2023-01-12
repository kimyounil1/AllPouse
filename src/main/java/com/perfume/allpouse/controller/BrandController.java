package com.perfume.allpouse.controller;


import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.model.dto.*;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.ListResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.*;
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
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.BRAND;
import static com.perfume.allpouse.model.enums.Permission.ROLE_PERFUMER;
import static com.perfume.allpouse.model.enums.Permission.ROLE_USER;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class BrandController {

    private final Logger LOGGER = LoggerFactory.getLogger(BrandController.class);

    private final TokenProvider tokenProvider;

    private final ResponseService responseService;

    private final BrandService brandService;

    private final PerfumeService perfumeService;

    private final BoardLogService boardLogService;

    private final PhotoService photoService;

    private final ReviewService reviewService;


    // 브랜드 페이지
    @GetMapping("brand/{brandId}")
    @Operation(summary = "브랜드 및 리뷰 페이지", description = "브랜드 상세 페이지. 기본 정보 및 리뷰 제공.")
    public SingleResponse<BrandDetails<BrandInfoDto>> getBrandPage(HttpServletRequest request, @ApiParam(value = "브랜드 id", required = true) @PathVariable Long brandId,
                                                      @ApiIgnore @PageableDefault(page = 0, size = 5, sort = "recommendCnt", direction = Sort.Direction.DESC) Pageable pageable) {

        final int size = 5;

        long id = tokenProvider.getId(tokenProvider.resolveToken(request));

        // 브랜드 정보
        BrandInfoDto brandInfo = brandService.getBrandInfo(brandId);

        // 브랜드 향수 size개(조회수 기준 내림차순)
        List<PerfumeResponseDto> perfumeList = perfumeService.getPerfumeListByBrandId(brandId, size);

        // 조향사 리뷰
        List<ReviewResponseDto> perfumerReviews = reviewService.getReviewsOnBrand(brandId, ROLE_PERFUMER, size);

        // 일반 유저 리뷰
        List<ReviewResponseDto> userReviews = reviewService.getReviewsOnBrand(brandId, ROLE_USER, size);

        // 가장 추천수 많은 리뷰
        List<ReviewResponseDto> bestReviewsOnPerfume = reviewService.getReviewsOnBrand(brandId, pageable);


        BrandDetails<BrandInfoDto> brandDetails = new BrandDetails<>(brandInfo, perfumeList, perfumerReviews, userReviews, bestReviewsOnPerfume);

        boardLogService.save(boardLogService.setSuccessLog(Thread.currentThread().getStackTrace()[1].getClassName(),Thread.currentThread().getStackTrace()[1].getMethodName(),
                "get single brand detail",brandId,id));

        return responseService.getSingleResponse(brandDetails);
    }


    // 브랜드 추가
    @PostMapping("brand")
    @Operation(summary = "<사용X> 브랜드 저장 및 수정", description = "***<데이터 추가용> 브랜드 저장하거나 수정하는 API")
    public CommonResponse saveAndUpdateBrand(
            @ApiParam(value = "브랜드 사진 담는 DTO", required= true) @RequestPart SaveBrandDto saveBrandDto,
            @ApiParam(value = "브랜드 사진", required = false) @RequestPart(value = "photo", required = false) List<MultipartFile> photos) throws IOException {

        // 첨부사진 있는 경우
        if (photos != null) {
            brandService.save(saveBrandDto, photos);
        }
        // 첨부사진 없는 경우
        else {
            brandService.save(saveBrandDto);
        }
        return responseService.getSuccessCommonResponse();
    }


    // 브랜드 삭제
    @DeleteMapping("brand")
    @Operation(summary = "브랜드 삭제", description = "brandId로 브랜드 삭제하는 API")
    public CommonResponse deleteBrand(@ApiParam(value = "삭제하려는 브랜드 id", required = true) @RequestParam Long brandId) {

        brandService.delete(brandId);

        photoService.delete(BRAND, brandId);

        return responseService.getSuccessCommonResponse();
    }

    // 브랜드 10개
    @GetMapping("brand")
    @Operation(summary = "(임시)브랜드 10개", description = "브랜드 10개 간단 정보 가져오는 API")
    public ListResponse<BrandResponseDto> getBrandList() {

        final int size = 10;
        List<BrandResponseDto> brandDtoList = brandService.findAllWithSize(size);

        return responseService.getListResponse(brandDtoList);
    }
}
