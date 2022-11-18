package com.perfume.allpouse.service;

import com.perfume.allpouse.service.dto.SaveBrandDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BrandServiceTest {

    @Autowired
    BrandService brandService;

    @Test
    @DisplayName("브랜드 저장 테스트")
    @Transactional
    public void saveBrandTest() throws Exception{
        //given
        SaveBrandDto brandDto = new SaveBrandDto("dior", "luxury_perfume", "path_123");

        //when
        Long brandId = brandService.save(brandDto);

        //then
        assertThat(brandId).isEqualTo(1);
    }


    @Test
    @DisplayName("브랜드 수정 테스트")
    @Transactional
    @Rollback(false)
    public void updateBrandTest() throws Exception{
        //given
        SaveBrandDto brandDto = new SaveBrandDto("dior", "luxury_perfume", "path_123");
        Long brandId = brandService.save(brandDto);

        //when
        SaveBrandDto newBrandDto = new SaveBrandDto(brandId, "new_brand", "new_perfume", "new_path");
        Long newId = brandService.update(newBrandDto);

        //then
        Assertions.assertThat(newId).isEqualTo(brandId);
    }

}