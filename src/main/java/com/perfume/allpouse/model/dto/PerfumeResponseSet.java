package com.perfume.allpouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PerfumeResponseSet {

    private List<PerfumeResponseDto> perfumes;

    private int perfumeCnt;
}
