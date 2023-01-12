package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.repository.BrandRepository;
import com.perfume.allpouse.model.dto.SearchBrandDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class BrandRepositoryImplTest {

    @Autowired
    BrandRepository brandRepository;



    @Test
    @DisplayName("브랜드 기본검색 테스트")
    public void basic_brand_search_test() throws Exception{
        //given
        String keyword = "cha";

        //when
        List<SearchBrandDto> search = brandRepository.search(keyword);

        //then
        Assertions.assertThat(search.size()).isEqualTo(1);
        System.out.println(search);
    }


    @Test
    @DisplayName("조회수 업데이트 테스트")
    @Transactional
    @Rollback(false)
    public void hitCntUpdateTest() throws Exception{
        //given
        Long id = 1478L;

        brandRepository.updateHitCnt(id);

        Brand brand = brandRepository.findById(id).get();
        System.out.println(brand.getHitCnt());

    }
}