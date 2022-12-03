package com.perfume.allpouse.model.reponse;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageResponse extends CommonResponse {
    private Page pages;
}
