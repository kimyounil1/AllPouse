package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.PerfumerApplication;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.model.dto.PerfumerApplicationForm;
import com.perfume.allpouse.service.PerfumerApplicationService;
import com.perfume.allpouse.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerfumerApplicationServiceImpl implements PerfumerApplicationService {

    private final Logger LOGGER = LoggerFactory.getLogger(PerfumerApplication.class);

    private final PhotoService photoService;

    private final UserRepository userRepository;


    @Override
    @Transactional
    public Long save(PerfumerApplicationForm form, MultipartFile file) {
        return null;
    }

    @Override
    @Transactional
    public void approve(Long applicationId) {

    }
}
