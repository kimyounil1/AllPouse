package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.repository.PerfumeBoardRepository;
import com.perfume.allpouse.service.dto.SaveBrandDto;
import com.perfume.allpouse.service.dto.SavePerfumeDto;
import com.perfume.allpouse.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class PerfumeServiceTest {

    @Autowired
    BrandServiceImpl brandService;

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
        Long perfumeId = savePerfume(brandId, "subject");

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
        Long perfumeId = savePerfume(brandId, "subject");

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
        Long perfumeId = savePerfume(brandId, "subject");

        //when
        perfumeService.delete(perfumeId);
        Optional<PerfumeBoard> findPerfume = perfumeRepository.findById(perfumeId);

        //then
        assertThat(findPerfume).isNotPresent();
        assertThrows(IllegalStateException.class,
                () -> perfumeService.delete(10L));
    }


    @Test
    @Transactional
    @DisplayName("브랜드 pk로 향수 검색하기")
    @Rollback(false)
    public void findByBrandIdTest() throws Exception{
        //given
        Long brandId = saveBrand();

        Long perfume1 = savePerfume(brandId, "perfume1");
        Long perfume2 = savePerfume(brandId, "perfume2");
        Long perfume3 = savePerfume(brandId, "asdf");

        //when
        List<PerfumeBoard> perfumes = perfumeService.findByBrandId(brandId);

        //then
        assertThat(perfumes.size()).isEqualTo(3);
        assertThat(perfumes.get(0).getSubject()).isEqualTo("asdf");
    }




    private Long savePerfume(Long brandId, String subject) {
        SavePerfumeDto perfumeDto = new SavePerfumeDto(subject, "content", 10000, brandId, "path");
        return perfumeService.save(perfumeDto);
    }

    private Long saveBrand() {
        SaveBrandDto brandDto = new SaveBrandDto("brand_1", "content", "path");
        return brandService.save(brandDto);
    }

}