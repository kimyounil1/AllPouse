package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.model.dto.PerfumerApplicationForm;
import com.perfume.allpouse.model.dto.PerfumerApplicationResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class PerfumerApplicationServiceImplTest {

    @Autowired
    PerfumerApplicationServiceImpl applicationService;


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