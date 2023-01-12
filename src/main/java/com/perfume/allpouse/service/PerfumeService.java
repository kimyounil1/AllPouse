package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.model.dto.PerfumeInfoDto;
import com.perfume.allpouse.model.dto.PerfumeResponseDto;
import com.perfume.allpouse.model.dto.SavePerfumeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PerfumeService {

    // 저장 / 업데이트 / 삭제
    Long save(SavePerfumeDto savePerfumeDto, List<MultipartFile> photos) throws IOException;

    Long save(SavePerfumeDto savePerfumeDto);

    Long update(SavePerfumeDto savePerfumeDto);

    void delete(Long id);

    // 조회

    List<PerfumeBoard> findAll();

    List<PerfumeResponseDto> findAllWithSize(int size);

    PerfumeInfoDto getPerfumeInfo(Long id);

    List<PerfumeResponseDto> getPerfumeListByBrandId(Long BrandId, int size);

    PerfumeBoard findById(Long id);

    Page<PerfumeResponseDto> getPerfumeByHitCnt(Pageable pageable);


    // 기타

    void addHitCnt(Long id);
}
