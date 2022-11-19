package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.service.dto.SaveBrandDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        assertThat(newId).isEqualTo(brandId);
    }


    @Test
    @Transactional
    @DisplayName("전체 브랜드 조회")
    public void findAllTest() throws Exception{
        //given
        SaveBrandDto brandDto1 = new SaveBrandDto("dior", "luxury_perfume", "path_123");
        Long brandId1 = brandService.save(brandDto1);

        SaveBrandDto brandDto2 = new SaveBrandDto("chanel", "luxury_perfume", "path_123");
        Long brandId2 = brandService.save(brandDto2);

        //when
        List<Brand> brands = brandService.findAll();

        //then
        assertThat(brands.size()).isEqualTo(2);
    }


    @Test
    @Transactional
    @DisplayName("ID로 브랜드 조회")
    public void findByIdTest() throws Exception{
        //given
        SaveBrandDto brandDto1 = new SaveBrandDto("dior", "luxury_perfume", "path_123");
        Long brandId1 = brandService.save(brandDto1);

        SaveBrandDto brandDto2 = new SaveBrandDto("chanel", "luxury_perfume", "path_123");
        Long brandId2 = brandService.save(brandDto2);

        //when
        Brand findBrand = brandService.findOne(brandId1);

        //then

        //DB에 있는 브랜드 id 검색 -> 해당 브랜드 반환
        assertThat(findBrand.getName()).isEqualTo("dior");

        //DB에 없는 브랜드 id 검색 -> IllegalStateException
        assertThrows(IllegalStateException.class,
                ()->brandService.findOne(101L));
    }

    @Test
    @Transactional
    @DisplayName("이름으로 브랜드 조회")
    public void findByNameTest() throws Exception{
        //given
        SaveBrandDto brandDto1 = new SaveBrandDto("dior", "luxury_perfume", "path_123");
        Long brandId1 = brandService.save(brandDto1);


        SaveBrandDto brandDto2 = new SaveBrandDto("diorhanel", "luxury_perfume", "path_123");
        Long brandId2 = brandService.save(brandDto2);

        SaveBrandDto brandDto3 = new SaveBrandDto("chanel", "luxury_perfume", "path_123");
        Long brandId3 = brandService.save(brandDto3);


        //when
        List<Brand> brands = brandService.find("dior");


        //then
        assertThat(brands.size()).isEqualTo(2);
        assertThat(brands.get(0).getName()).isEqualTo("dior");

    }
}