package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.repository.BrandRepository;
import com.perfume.allpouse.model.dto.PerfumerApplicationForm;
import com.perfume.allpouse.model.dto.PerfumerApplicationResponseDto;
import com.perfume.allpouse.model.dto.SaveBrandDto;
import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.service.PerfumeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    @Transactional
    @Rollback(false)
    public void mapTest() throws Exception {

        SavePerfumeDto savePerfumeDto = new SavePerfumeDto("test subject", "test content", 10000, 109L);

        Long perfumeId = perfumeService.save(savePerfumeDto);

        //when
        PerfumeBoard perfume = perfumeService.findById(perfumeId);
        perfume.setScore(new HashMap<>());

        Map<String, Long> score = perfume.getScore();
        System.out.println(score);

        score.put("persistence", 0L);
        score.put("cost-effectiveness", 0L);
        System.out.println(perfume.getScore());

    }
}