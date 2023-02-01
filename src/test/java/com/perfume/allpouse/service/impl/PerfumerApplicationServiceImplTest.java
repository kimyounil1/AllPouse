package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.repository.BrandRepository;
import com.perfume.allpouse.model.dto.PerfumerApplicationForm;
import com.perfume.allpouse.model.dto.PerfumerApplicationResponseDto;
import com.perfume.allpouse.model.dto.SaveBrandDto;
import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.service.PerfumeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PerfumerApplicationServiceImplTest {

    @Autowired
    PerfumerApplicationServiceImpl applicationService;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    PerfumeService perfumeService;


    @Test
    @Transactional
    @Rollback(false)
    public void saveTest() throws Exception {

        PerfumerApplicationForm form = PerfumerApplicationForm.builder()
                .text("테스트용 텍스트")
                .userId(5L)
                .build();

        Long save = applicationService.save(form);
        System.out.println(save);
    }

    @Test
    @Transactional
    public void findAllTest_2() throws Exception{
        //given
        List<PerfumerApplicationResponseDto> list = applicationService.getApplicationList();

        //when
        System.out.println(list);

        //then

    }



}