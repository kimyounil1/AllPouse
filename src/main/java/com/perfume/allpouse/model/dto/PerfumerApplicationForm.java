package com.perfume.allpouse.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PerfumerApplicationForm {

    private Long id;

    private Long userId;

    private String text;

}
