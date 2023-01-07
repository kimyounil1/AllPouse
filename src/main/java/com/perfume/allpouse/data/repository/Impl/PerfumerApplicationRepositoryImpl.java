package com.perfume.allpouse.data.repository.Impl;


import com.perfume.allpouse.data.entity.PerfumerApplication;
import com.perfume.allpouse.data.entity.QPerfumerApplication;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.repository.custom.PerfumerApplicationRepositoryCustom;
import com.perfume.allpouse.model.dto.PerfumerApplicationResponseDto;
import com.perfume.allpouse.model.dto.QPerfumerApplicationResponseDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.PERFUMER_APPLICATION;

public class PerfumerApplicationRepositoryImpl extends QuerydslRepositorySupport implements PerfumerApplicationRepositoryCustom {

    QPerfumerApplication application = QPerfumerApplication.perfumerApplication;
    QPhoto photo = QPhoto.photo;

    public PerfumerApplicationRepositoryImpl() {
        super(PerfumerApplication.class);
    }


    // 전체 신청 내역 조회
    @Override
    public List<PerfumerApplicationResponseDto> getAllApplication() {

        List<PerfumerApplicationResponseDto> applications = from(application)
                .leftJoin(photo)
                .on(application.id.eq(photo.boardId).and(photo.boardType.eq(PERFUMER_APPLICATION)))
                .select(new QPerfumerApplicationResponseDto(
                        application.id,
                        application.user.id,
                        application.user.userName,
                        photo.path,
                        application.text,
                        application.isApproved,
                        application.createDateTime))
                .orderBy(application.createDateTime.asc())
                .fetch();

        return applications;
    }


    // 승인된 신청 내역 조회
    @Override
    public List<PerfumerApplicationResponseDto> getApprovedApplications() {

        return getResponseDtoByApproval(true);
    }


    // 미승인된 신청 내역 조회
    @Override
    public List<PerfumerApplicationResponseDto> getNotApprovedApplications() {

        return getResponseDtoByApproval(false);
    }




    private List<PerfumerApplicationResponseDto> getResponseDtoByApproval(boolean approvalStatus) {

        List<PerfumerApplicationResponseDto> applications = from(application)
                .leftJoin(photo)
                .on(application.id.eq(photo.boardId).and(photo.boardType.eq(PERFUMER_APPLICATION)))
                .where(application.isApproved.eq(approvalStatus))
                .select(new QPerfumerApplicationResponseDto(
                        application.id,
                        application.user.id,
                        application.user.userName,
                        photo.path,
                        application.text,
                        application.isApproved,
                        application.createDateTime))
                .orderBy(application.createDateTime.asc())
                .fetch();

        return applications;
    }
}
