package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Photo;
import com.perfume.allpouse.data.repository.PhotoRepository;
import com.perfume.allpouse.service.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.REVIEW;

@SpringBootTest
class S3ServiceImplTest {

    @Autowired
    S3Service s3Service;

    @Autowired
    PhotoRepository photoRepository;

    @Test
    @DisplayName("삭제테스트")
    @Transactional
    @Rollback(false)
    public void deleteTest() throws Exception{

        Long reviewId = 425L;
        //https://perfume-log.s3.ap-northeast-2.amazonaws.com/6d0485a7-57fe-4e9d-97d3-cecaca307594-IMG_6928105651717215532.webp


        Photo photo = photoRepository.findPhotoByBoardTypeAndBoardId(REVIEW, reviewId);
        List<String> pathList = photo.getPath();


        for (String path : pathList) {
            s3Service.delete(path);
        }
        photoRepository.delete(photo);
    }


}