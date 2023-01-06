package com.perfume.allpouse.service;

import com.perfume.allpouse.model.enums.BoardType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    // 사진 하나일 때 저장
    String save(MultipartFile file, BoardType boardType, Long boardId) throws IOException;

    // 사진 여러개 저장
    List<String> save(List<MultipartFile> multipartFileList, BoardType boardType, Long boardId) throws IOException;

    void delete(BoardType type, Long boardId);

    List<String> getImagePath(BoardType type, Long boardId);

    List<String> update(List<MultipartFile> multipartFileList, BoardType boardType, Long boardId) throws IOException;
}
