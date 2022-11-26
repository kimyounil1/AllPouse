package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.controller.ReviewController;
import com.perfume.allpouse.data.entity.Photo;
import com.perfume.allpouse.data.repository.PhotoRepository;
import com.perfume.allpouse.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PhotoServiceImpl implements PhotoService {

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private final S3ServiceImpl s3ServiceImpl;

    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(S3ServiceImpl s3ServiceImpl, PhotoRepository photoRepository) {
        this.s3ServiceImpl = s3ServiceImpl;
        this.photoRepository = photoRepository;
    }

    @Override
    public List<String> save(List<MultipartFile> multipartFileList, String type, Long boardId) throws IOException {
        List<String> result = s3ServiceImpl.upload(multipartFileList);
        for ( String path : result) {
            Photo photo = Photo.builder()
                    .boardId(boardId)
                    .path(path)
                    .type(type)
                    .build();
            photoRepository.save(photo);
            LOGGER.info("[save] 사진 Entity 저장 완료");
        }
        
        return result;
    }




}
