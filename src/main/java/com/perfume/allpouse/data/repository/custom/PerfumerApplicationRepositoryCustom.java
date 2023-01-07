package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.PerfumerApplicationResponseDto;

import java.util.List;

public interface PerfumerApplicationRepositoryCustom {

    /**
     * 조회 : PerfumerApplicationResponseDto로 가져옴
     */

    // 1. 전체 내역
    List<PerfumerApplicationResponseDto> getAllApplication();

    // 2. 승인 내역
    List<PerfumerApplicationResponseDto> getApprovedApplications();

    // 3. 미승인 내역
    List<PerfumerApplicationResponseDto> getNotApprovedApplications();

}
