package com.perfume.allpouse.data.repository;


import com.perfume.allpouse.data.entity.PerfumerApplication;
import com.perfume.allpouse.data.repository.custom.PerfumerApplicationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumerApplicationRepository extends JpaRepository<PerfumerApplication, Long>, PerfumerApplicationRepositoryCustom {

    // 전체 내역 조회
    List<PerfumerApplication> findPerfumerApplicationsByUserNotNullOrderByCreateDateTimeAsc();

    // 승인 내역 조회
    List<PerfumerApplication> findPerfumerApplicationsByIsApprovedIsTrueOrderByCreateDateTimeAsc();

    // 미승인 내역 조회
    List<PerfumerApplication> findPerfumerApplicationsByIsApprovedIsFalseOrderByCreateDateTimeAsc();
}
