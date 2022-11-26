package com.perfume.allpouse.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@ToString
public class PhotoDto {

    private String type;

    private Long boardId;

    private String path;

}
