package com.perfume.allpouse.service;

import com.perfume.allpouse.service.dto.SaveBrandDto;
import com.perfume.allpouse.service.dto.SavePerfumeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PerfumeServiceTest {

    @Autowired
    BrandService brandService;

    @Autowired
    PerfumeService perfumeService;

    @Test
    @Transactional
    @DisplayName("향수 저장 테스트")
    @Rollback(false)
    public void saveTest() throws Exception{
        //given
        Long brandId = saveBrand();
        Long perfumeId = savePerfume(brandId);

        //when


        //then
        assertThat(perfumeId).isNotNull();

    }



    private Long savePerfume(Long brandId) {
        SavePerfumeDto perfumeDto = new SavePerfumeDto("subject", "content", 10000, brandId, "path");
        return perfumeService.save(perfumeDto);
    }

    private Long saveBrand() {
        SaveBrandDto brandDto = new SaveBrandDto("brand_1", "content", "path");
        return brandService.save(brandDto);
    }

}