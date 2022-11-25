package com.perfume.allpouse.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    List<String> upload(List<MultipartFile> multipartFileList) throws IOException;

}
