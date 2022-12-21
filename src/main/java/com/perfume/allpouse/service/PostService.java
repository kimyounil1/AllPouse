package com.perfume.allpouse.service;

import com.perfume.allpouse.model.dto.SavePostDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Long save(SavePostDto savePostDto);

    Long save(SavePostDto savePostDto, List<MultipartFile> photos) throws IOException;

    Long update(SavePostDto savePostDto);


}
