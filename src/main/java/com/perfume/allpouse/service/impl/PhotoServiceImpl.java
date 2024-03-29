package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.controller.ReviewController;
import com.perfume.allpouse.data.entity.Photo;
import com.perfume.allpouse.data.repository.PhotoRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.exception.ExceptionEnum;
import com.perfume.allpouse.model.enums.BoardType;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.S3Service;
import com.perfume.allpouse.utils.StringListConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.perfume.allpouse.exception.ExceptionEnum.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoServiceImpl implements PhotoService {

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private final S3Service s3ServiceImpl;

    private final PhotoRepository photoRepository;


    // 사진 여러개일 때 저장
    @Override
    @Transactional
    public List<String> save(List<MultipartFile> multipartFileList, BoardType boardType, Long boardId) throws IOException {
        List<String> result = s3ServiceImpl.upload(multipartFileList);

        Photo photo = Photo.builder()
                .boardId(boardId)
                .path(result)
                .boardType(boardType)
                .build();

        photoRepository.save(photo);
        LOGGER.info("[save] 사진 Entity 저장 완료");

        return result;
    }


    @Override
    @Transactional
    public List<String> update(List<MultipartFile> multipartFileList, BoardType boardType, Long boardId) throws IOException {
        delete(boardType,boardId);
        List<String> result = s3ServiceImpl.upload(multipartFileList);

        Photo photo = Photo.builder()
                .boardId(boardId)
                .path(result)
                .boardType(boardType)
                .build();

        photoRepository.save(photo);
        LOGGER.info("[save] 사진 Entity 저장 완료");

        return result;
    }


    @Override
    @Transactional
    public void delete(BoardType type, Long boardId) {

        Photo photo = photoRepository.findPhotoByBoardTypeAndBoardId(type, boardId);

        if (photo != null) {

            List<String> pathList = photo.getPath();

            for (String path : pathList) {
                s3ServiceImpl.delete(path);
            }

            photoRepository.delete(photo);
        }
    }


    public List<String> getImagePath(BoardType type, Long boardId) {

        Photo photo = photoRepository.findPhotoByBoardTypeAndBoardId(type, boardId);

        List<String> path = photo.getPath();

        return path;
    }



    public boolean getExistsImage(BoardType type, Long boardId) {

        boolean result = photoRepository.exists(type, boardId);

        return result;
    }

}
