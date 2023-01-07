package com.perfume.allpouse.controller;


import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.model.dto.PerfumerApplicationForm;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.service.PerfumerApplicationService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class ApplyController {

    private final Logger LOGGER = LoggerFactory.getLogger(ApplyController.class);

    private final TokenProvider tokenProvider;

    private final PerfumerApplicationService perfumerApplicationService;



    // 조향사 신청 등록
    @PostMapping(value = "perfumer-application", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "조향사 신청 등록", description = "조향사 신청 등록하는 API")
    public CommonResponse applyPerfumer(
            HttpServletRequest request,
            @ApiParam(value = "등록 정보", required = true) @RequestPart PerfumerApplicationForm form,
            @ApiParam(value = "등록 서류 사진 파일", required = true) @RequestPart(value = "photo") MultipartFile photo) {



    }


    // 조향사 신청 화면


    // 조향사 신청 승인


}
