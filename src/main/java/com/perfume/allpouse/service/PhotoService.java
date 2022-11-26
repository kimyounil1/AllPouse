package com.perfume.allpouse.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    List<String> save(List<MultipartFile> multipartFileList, String type, Long boardId) throws IOException;

}
