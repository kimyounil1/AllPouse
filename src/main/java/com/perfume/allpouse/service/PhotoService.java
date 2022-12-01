package com.perfume.allpouse.service;

import com.perfume.allpouse.model.enums.BoardType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    List<String> save(List<MultipartFile> multipartFileList, BoardType boardType, Long boardId) throws IOException;

    void delete(BoardType type, Long boardId);

    List<String> getImagePath(BoardType type, Long boardId);

    List<String> update(List<MultipartFile> multipartFileList, BoardType boardType, Long boardId) throws IOException;
}
