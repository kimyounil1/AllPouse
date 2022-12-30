package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.model.dto.SearchPerfumeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PerfumeRepositoryImplTest {

    @Autowired
    PerfumeBoardRepository perfumeBoardRepository;


    @Test
    @DisplayName("향수 기본검색 테스트")
    public void basic_perfume_search_test() throws Exception{
        //given
        String keyword = "dio";

        //when
        List<SearchPerfumeDto> search = perfumeBoardRepository.search(keyword);

        //then
        System.out.println(search);

    }



}