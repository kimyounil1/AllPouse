package com.perfume.allpouse.service;


import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.repository.BrandRepository;
import com.perfume.allpouse.repository.PerfumeBoardRepository;
import com.perfume.allpouse.service.dto.SavePerfumeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerfumeService {

    private final PerfumeBoardRepository perfumeRepository;
    private final BrandRepository brandRepository;


    // 향수 저장
    @Transactional
    public Long save(SavePerfumeDto savePerfumeDto) {

        PerfumeBoard perfumeBoard = toEntity(savePerfumeDto);
        PerfumeBoard savedPerfume = perfumeRepository.save(perfumeBoard);

        return savedPerfume.getId();
    }


    // 향수 수정
    @Transactional
    public Long update(SavePerfumeDto savePerfumeDto) {

        PerfumeBoard perfume = perfumeRepository.findById(savePerfumeDto.getId()).get();
        perfume.changePerfume(savePerfumeDto);

        return perfume.getId();
    }


    // 향수 삭제
    @Transactional
    public void delete(Long id) {

        Optional<PerfumeBoard> perfume = perfumeRepository.findById(id);

        if (perfume.isPresent()) {
            perfumeRepository.deleteById(id);
        } else {
            throw new IllegalStateException("삭제할 향수가 없습니다.");
        }
    }


    // Dto -> PerfumeBoard
    private PerfumeBoard toEntity(SavePerfumeDto perfumeDto) {

        Brand brand = brandRepository.findById(perfumeDto.getBrandId()).get();

        PerfumeBoard perfume = PerfumeBoard.builder()
                .id(perfumeDto.getId())
                .subject(perfumeDto.getSubject())
                .content(perfumeDto.getContent())
                .price(perfumeDto.getPrice())
                .imagePath(perfumeDto.getImagePath())
                .build();

        perfume.addBrand(brand);

        return perfume;
    }



}