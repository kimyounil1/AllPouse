package com.perfume.allpouse.service;


import com.perfume.allpouse.data.entity.Photo;
import com.perfume.allpouse.data.repository.PhotoRepository;
import com.perfume.allpouse.model.enums.BoardType;
import com.perfume.allpouse.service.impl.PhotoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class PhotoServiceTest {
    @Autowired
    PhotoServiceImpl photoServiceImpl;

    @Autowired
    PhotoRepository photoRepository;

    @Test
    @Transactional
    @DisplayName("사진 삭제 테스트")
    public void saveTest() throws Exception{

        photoServiceImpl.delete(BoardType.REVIEW, 13L);

        try {

            List<Photo> photos = photoRepository.findAll();
            Assertions.assertThat(photos).isEmpty();
        } catch (NoSuchElementException e) {
            System.out.println("사진 없음");
        }
    }
}
