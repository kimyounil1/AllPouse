package com.perfume.allpouse.controller;


import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.model.dto.PerfumerApplicationForm;
import com.perfume.allpouse.model.dto.PerfumerApplicationResponseDto;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.ListResponse;
import com.perfume.allpouse.service.PerfumerApplicationService;
import com.perfume.allpouse.service.ResponseService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class ApplyController {

    private final Logger LOGGER = LoggerFactory.getLogger(ApplyController.class);

    private final TokenProvider tokenProvider;

    private final PerfumerApplicationService perfumerApplicationService;

    private final ResponseService responseService;


    // 조향사 신청 등록
    @PostMapping(value = "perfumer-application", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "조향사 등급 신청 등록", description = "조향사 신청 등록하는 API. 등록 서류 사진 파일은 1개만 첨부해주세요.")
    public CommonResponse applyPerfumer(
            HttpServletRequest request,
            @ApiParam(value = "등록 정보", required = true) @RequestPart PerfumerApplicationForm form,
            @ApiParam(value = "등록 서류 사진 파일", required = true) @RequestPart(value = "photo") List<MultipartFile> photo) throws IOException
    {

        Long userId = tokenProvider.getUserIdFromRequest(request);
        form.setUserId(userId);
        perfumerApplicationService.save(form, photo);

        return responseService.getSuccessCommonResponse();
    }


    // 조향사 신청 내역 - 전체
    @GetMapping(value = "perfumer-application/all")
    @Operation(summary = "전체 조향사 등급 신청 내역", description = "조향사 신청 내역 전부(승인/미승인)를 보여주는 API")
    public ListResponse<PerfumerApplicationResponseDto> getAllPerfumerApplicationList() {

        List<PerfumerApplicationResponseDto> applicationList = perfumerApplicationService.getApplicationList();

        return responseService.getListResponse(applicationList);
    }


    // 조향사 신청 내역 - 승인
    @GetMapping(value = "perfumer-application/approved")
    @Operation(summary = "승인된 조향사 등급 신청 내역", description = "조향사 등급 신청 내역 중 승인된 내역만 보여주는 API")
    public ListResponse<PerfumerApplicationResponseDto> getApprovedPerfumerApplicationList() {

        List<PerfumerApplicationResponseDto> applicationList = perfumerApplicationService.getApprovedApplicationList();

        return responseService.getListResponse(applicationList);
    }


    // 조향사 신청 내역 - 미승인
    @GetMapping(value = "perfumer-application/not-approved")
    @Operation(summary = "미승인된 조향사 등급 신청 내역", description = "조향사 등급 신청 내역 중 미승인된 내역만 보여주는 API")
    public ListResponse<PerfumerApplicationResponseDto> getNotApprovedPerfumerApplicationList() {

        List<PerfumerApplicationResponseDto> applicationList = perfumerApplicationService.getNotApprovedApplicationList();

        return responseService.getListResponse(applicationList);
    }


    // 조향사 신청 승인
    @PostMapping(value = "perfumer/{applicationId}")
    @Operation(summary = "조향사 등급 승인", description = "조향사 등급 승인 API")
    public CommonResponse approvePerfumer(@ApiParam(value = "조향사 등급 신청 id", required = true) @PathVariable("applicationId") Long applicationId) {

        perfumerApplicationService.approve(applicationId);

        return responseService.getSuccessCommonResponse();
    }


    // 조향사 신청 거절

}
