package com.perfume.allpouse.service;

import com.perfume.allpouse.model.dto.PerfumerApplicationForm;
import org.springframework.web.multipart.MultipartFile;

public interface PerfumerApplicationService {

    // [조향사 신청] 등록(저장)
    Long save(PerfumerApplicationForm form, MultipartFile file);

    // [조향사 신청] 승인
    void approve(Long applicationId);

}
