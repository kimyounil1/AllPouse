package com.perfume.allpouse.service;

import com.perfume.allpouse.model.dto.PerfumerApplicationForm;
import com.perfume.allpouse.model.dto.PerfumerApplicationResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PerfumerApplicationService {

    /*
     등록 / 삭제 / 승인 / 거절
     */

    // [조향사 신청] 등록(저장)
    Long save(PerfumerApplicationForm form, List<MultipartFile> file) throws IOException;


    // [조향사 신청] 삭제
    void delete(Long applicationId);


    // [조향사 신청] 승인
    void approve(Long applicationId);


    // [조향사 신청] 거절
    void deny(Long applicationId);


    /*
     조회
     */

    // [조향사 신청] 전체 내역 조회
    List<PerfumerApplicationResponseDto> getApplicationList();


    // [조향사 신청] 승인 내역 조회
    List<PerfumerApplicationResponseDto> getApprovedApplicationList();


    // [조향사 신청] 미승인 내역 조회
    List<PerfumerApplicationResponseDto> getNotApprovedApplicationList();






}
