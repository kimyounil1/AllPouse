package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.service.PerfumeService;
import com.perfume.allpouse.service.ReviewService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PerfumeServiceImplTest {

    @AfterAll
    static void afterAllMethod() {
        System.out.println("테스트 끝");
    }

    @Autowired
    PerfumeService perfumeService;

    @Autowired
    PerfumeBoardRepository perfumeRepository;
    
    @Autowired
    ReviewService reviewService;
    

    @Test
    @DisplayName("향수 점수 초기화 테스트")
    @Transactional
    public void perfumeScoreTest() throws Exception {
        //given
        Long brandId = 109L;

        // when
        SavePerfumeDto dto = new SavePerfumeDto("test subject", "test content", 10000, brandId);
        Long savedId = perfumeService.save(dto);
        perfumeService.delete(savedId);

        //then
        Assertions.assertThrows(
                IllegalStateException.class,
                ()-> perfumeService.findById(savedId)
        );
    }


    @Test
    @DisplayName("향수 점수 생성 및 삭제 테스트")
    @Transactional
    @Rollback(false)
    public void perfumeSaveAndDeleteTest() throws Exception {
        //given
        Long brandId = 109L;
        SavePerfumeDto dto = new SavePerfumeDto("test subject", "test content", 10000, brandId);
        Long savedId = perfumeService.save(dto);

        //when
        perfumeService.delete(savedId);

        //then

    }
    
    
    @Test
    @DisplayName("리뷰 점수의 향수 점수에의 반영 테스트")
    @Transactional
    public void scoreReflectionTest() throws Exception{
        //given
        Long brandId = 109L;
        SavePerfumeDto dto = new SavePerfumeDto("test subject", "test content", 10000, brandId);
        Long savedId = perfumeService.save(dto);

        // new SaveReviewDto("")
        
        //when
        
        //then


        
    }
}