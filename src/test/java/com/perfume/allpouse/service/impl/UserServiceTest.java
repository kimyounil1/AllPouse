package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.model.dto.UserInfoDto;
import com.perfume.allpouse.model.enums.BoardType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    PhotoServiceImpl photoService;

    @Test
    @DisplayName("UserInfo DTO 테스트")
    @Transactional
    public void userInfoDtoTest() throws Exception{

        UserInfoDto result = userService.getUserInfoDtoById(23L);
        System.out.println(result);

    }

    @Test
    @DisplayName("User Photo Upload 테스트")
    @Transactional
    public void userPhotoSaveTest() throws Exception{

        MockMultipartFile image = new MockMultipartFile("files", "silver.jpeg","image/jpeg","<<jpeg data>>".getBytes());
        List<MultipartFile> dataList = new ArrayList<>();
        Long saveId = Long.valueOf(23);
        dataList.add(image);

        List<String> resultList = photoService.save(dataList,BoardType.USER,saveId);
        System.out.println(resultList);

    }

    @Test
    @DisplayName("User Photo Delete 테스트")
    @Transactional
    public void userPhotoDeleteTest() throws Exception{

        // given

        Long savedId = Long.valueOf(23);

        //when
        photoService.delete(BoardType.USER,savedId);


        Assertions.assertThrows(IllegalStateException.class,
                ()->photoService.delete(BoardType.USER,savedId));

    }

}
