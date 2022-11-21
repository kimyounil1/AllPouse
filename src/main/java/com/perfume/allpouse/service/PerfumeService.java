package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.model.dto.SavePerfumeDto;

import java.util.List;

public interface PerfumeService {

    Long save(SavePerfumeDto savePerfumeDto);

    Long update(SavePerfumeDto savePerfumeDto);

    void delete(Long id);


    List<PerfumeBoard> findAll();

    PerfumeBoard findOne(Long id);

    List<PerfumeBoard> findByBrandId(Long id);
}
