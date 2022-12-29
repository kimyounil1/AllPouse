package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.repository.search.SearchBrandRepository;
import com.perfume.allpouse.model.dto.SearchBrandDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SearchBrandRepositoryImplTest {

    @Autowired
    SearchBrandRepository searchBrandRepository;

    @Test
    @DisplayName("브랜드 기본검색 테스트")
    public void basic_brand_search() throws Exception{
        //given
        String keyword = "cha";

        //when
        List<SearchBrandDto> search = searchBrandRepository.search(keyword);

        //then
        Assertions.assertThat(search.size()).isEqualTo(1);
        System.out.println(search);
    }





}