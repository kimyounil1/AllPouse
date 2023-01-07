package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.PerfumerApplication;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.PerfumerApplicationRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.PerfumerApplicationForm;
import com.perfume.allpouse.model.dto.PerfumerApplicationResponseDto;
import com.perfume.allpouse.service.PerfumerApplicationService;
import com.perfume.allpouse.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.perfume.allpouse.exception.ExceptionEnum.*;
import static com.perfume.allpouse.model.enums.BoardType.PERFUMER_APPLICATION;
import static com.perfume.allpouse.model.enums.Permission.ROLE_PERFUMER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PerfumerApplicationServiceImpl implements PerfumerApplicationService {

    private final Logger LOGGER = LoggerFactory.getLogger(PerfumerApplication.class);

    private final PerfumerApplicationRepository perfumerApplicationRepository;

    private final PhotoService photoService;

    private final UserRepository userRepository;



    // 신청폼/사진 저장
    @Override
    public Long save(PerfumerApplicationForm form, List<MultipartFile> file) throws IOException {

        PerfumerApplication application = perfumerApplicationRepository.save(toEntity(form));
        Long applicationId = application.getId();

        // 사진 저장
        photoService.save(file, PERFUMER_APPLICATION, applicationId);

        return applicationId;
    }

    public Long save(PerfumerApplicationForm form) throws IOException {

        PerfumerApplication application = perfumerApplicationRepository.save(toEntity(form));
        Long applicationId = application.getId();
        System.out.println("applicationId : " + applicationId);
        System.out.println("userId : " + application.getUser().getId());

        // 사진 저장
        //photoService.save(file, PERFUMER_APPLICATION, applicationId);

        return applicationId;
    }


    // 신청/사진 삭제
    // 승인 때 사용 X
    @Override
    public void delete(Long applicationId) {

        perfumerApplicationRepository.deleteById(applicationId);
        photoService.delete(PERFUMER_APPLICATION, applicationId);
    }


    // 신청 승인
    // (올바른 신청일 때) : 1. 신청서 승인상태 변경, 2. 유저 Permission 변경, 3. 승인 메시지 푸싱
    @Override
    public void approve(Long applicationId) {

        Optional<PerfumerApplication> applicationOpt = perfumerApplicationRepository.findById(applicationId);

        if (applicationOpt.isEmpty()) {
            throw new CustomException(INVALID_PARAMETER);
        } else {
            PerfumerApplication application = applicationOpt.get();
            User user = application.getUser();

            // 이미 '승인' 상태 ->
            if (application.isApproved()) {
                throw new CustomException(PERFUMER_APPLICATION_ID_NOT_FOUND);
            }
            // '미승인' 상태 -> '승인'으로 변경
            else  {

                // User.Permission 변경
                user.changePermission(ROLE_PERFUMER);

                // 승인여부 변경
                application.changeApproval(true);

                // 승인 메시지 푸싱

            }
        }
    }


    // 신청 거절
    // : 1. 신청 삭제하고, 2. 메시지 푸싱
    @Override
    public void deny(Long applicationId) {

        // 신청 삭제
        delete(applicationId);

        // 메시지 푸싱

    }


    // 전체 신청 내역 조회(승인 + 미승인)
    @Override
    public List<PerfumerApplicationResponseDto> getApplicationList() {
        return perfumerApplicationRepository.getAllApplication();
    }


    // 승인 내역 조회
    @Override
    public List<PerfumerApplicationResponseDto> getApprovedApplicationList() {
        return perfumerApplicationRepository.getApprovedApplications();
    }


    // 미승인 내역 조회
    @Override
    public List<PerfumerApplicationResponseDto> getNotApprovedApplicationList() {
        return perfumerApplicationRepository.getNotApprovedApplications();
    }


    // DTO(Form) -> Application
    private PerfumerApplication toEntity(PerfumerApplicationForm form) {


        System.out.println("시작");
        User user = userRepository.findById(form.getUserId()).get();

        PerfumerApplication application = PerfumerApplication.builder()
                .id(form.getId())
                .user(user)
                .text(form.getText())
                .isApproved(false)
                .build();

        System.out.println("유저 입력은됨");
        return application;
    }
}
