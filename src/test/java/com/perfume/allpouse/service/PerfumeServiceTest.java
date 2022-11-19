package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.repository.PerfumeBoardRepository;
import com.perfume.allpouse.service.dto.SaveBrandDto;
import com.perfume.allpouse.service.dto.SavePerfumeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PerfumeServiceTest {

    @Autowired
    BrandService brandService;

    @Autowired
    PerfumeService perfumeService;

    @Autowired
    PerfumeBoardRepository perfumeRepository;


    @Test
    @Transactional
    @DisplayName("향수 저장 테스트")
    public void saveTest() throws Exception{
        //given
        Long brandId = saveBrand();
        Long perfumeId = savePerfume(brandId);

        //when


        //then
        assertThat(perfumeId).isNotNull();

    }

    @Test
    @Transactional
    @DisplayName("향수 수정 테스트")
    public void updateTest() throws Exception{
        //given
        Long brandId = saveBrand();
        Long perfumeId = savePerfume(brandId);

        //when
        SavePerfumeDto newDto = new SavePerfumeDto(perfumeId, "new_subject", "new_content", 30000, brandId, "new_path");
        Long updatedId = perfumeService.update(newDto);

        //then
        assertThat(updatedId).isEqualTo(perfumeId);
    }


    @Test
    @Transactional
    public void deleteTest() throws Exception{
        //given
        Long brandId = saveBrand();
        Long perfumeId = savePerfume(brandId);

        //when
        perfumeService.delete(perfumeId);
        Optional<PerfumeBoard> findPerfume = perfumeRepository.findById(perfumeId);

        //then
        assertThat(findPerfume).isNotPresent();
        assertThrows(IllegalStateException.class,
                () -> perfumeService.delete(10L));
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