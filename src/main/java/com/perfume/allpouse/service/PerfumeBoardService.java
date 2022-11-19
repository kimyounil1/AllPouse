package com.perfume.allpouse.service;


import com.perfume.allpouse.repository.PerfumeBoardRepository;
import com.perfume.allpouse.service.dto.SavePerfumeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfumeBoardService {

    private final PerfumeBoardRepository perfumeRepository;



}
