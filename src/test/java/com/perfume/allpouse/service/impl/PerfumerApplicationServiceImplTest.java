package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.PerfumerApplication;
import com.perfume.allpouse.service.PerfumerApplicationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class PerfumerApplicationServiceImplTest {

    @Autowired
    PerfumerApplicationService applicationService;


    @Test
    @Transactional
    public void findAllTest() throws Exception {
        //given

        //when
        List<PerfumerApplication> list = applicationService.getApplicationList();

        //then
        Assertions.assertThat(list.size()).isEqualTo(1);

    }

}