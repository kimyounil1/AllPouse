package com.perfume.allpouse.service;


import com.perfume.allpouse.service.impl.PhotoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class PhotoServiceTest {
    @Autowired
    PhotoServiceImpl photoServiceImpl;

    @Test
    @Transactional
    @DisplayName("사진 저장 테스트")
    public void saveTest() throws Exception{
        //given
        MockMultipartFile image = new MockMultipartFile("files", "silver.jpeg","image/jpeg","<<jpeg data>>".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("files", "silver2.jpeg","image/jpeg","<<jpeg data>>".getBytes());
        List<MultipartFile> dataList = new ArrayList<>();

        dataList.add(image);
        dataList.add(image2);
        //when

        List<String> url = photoServiceImpl.save(dataList);

        //then

        System.out.println(url);
    }


}
