package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.entity.enumDirectory.LoginType;
import com.perfume.allpouse.data.entity.enumDirectory.Permission;
import com.perfume.allpouse.repository.BrandRepository;
import com.perfume.allpouse.repository.PerfumeBoardRepository;
import com.perfume.allpouse.repository.ReviewBoardRepository;
import com.perfume.allpouse.repository.UserRepository;
import com.perfume.allpouse.service.dto.SaveReviewDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewBoardRepository reviewBoardRepository;

    @Autowired
    PerfumeBoardRepository perfumeBoardRepository;

    @Autowired
    BrandRepository brandRepository;

    @Test
    @Rollback(false)
    @DisplayName("리뷰 저장 테스트")
    public void reviewSaveTest1() throws Exception{
        //given
        Brand brand = Brand.builder()
                .name("chanel")
                .content("content")
                .imagePath("imagepath")
                .build();

        Brand savedBrand = brandRepository.save(brand);
        Long brandId = savedBrand.getId();



        User user = User.builder()
                .socialId("dongdong")
                .userName("dong")
                .age(27)
                .gender("남성")
                .loginType(LoginType.KAKAOTALK)
                .permission(Permission.GENERAL)
                .build();

        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();


        PerfumeBoard perfume = PerfumeBoard.builder()
                .subject("asdf")
                .content("asdf")
                .price(10000)
                .imagePath("asdf")
                .brand(brand)
                .build();

        PerfumeBoard savedPerfume = perfumeBoardRepository.save(perfume);
        Long perfumeId = savedPerfume.getId();

        SaveReviewDto saveReviewDto = new SaveReviewDto("asdf", "content", userId, perfumeId, "impagePath");
        Long savedId = reviewService.save(saveReviewDto);

        assertThat(user.getReviews().size()).isEqualTo(1);
        assertThat(perfume.getReviews().size()).isEqualTo(1);
    }

    @Test
    @Rollback(false)
    @DisplayName("리뷰 업데이트 테스트 - 변경감지 이용")
    public void reviewUpdateTest() throws Exception{
        //given
        Brand brand = Brand.builder()
                .name("chanel")
                .content("content")
                .imagePath("imagepath")
                .build();

        Brand savedBrand = brandRepository.save(brand);
        Long brandId = savedBrand.getId();



        User user = User.builder()
                .socialId("dongdong")
                .userName("dong")
                .age(27)
                .gender("남성")
                .loginType(LoginType.KAKAOTALK)
                .permission(Permission.GENERAL)
                .build();

        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();


        PerfumeBoard perfume = PerfumeBoard.builder()
                .subject("asdf")
                .content("asdf")
                .price(10000)
                .imagePath("asdf")
                .brand(brand)
                .build();

        PerfumeBoard savedPerfume = perfumeBoardRepository.save(perfume);
        Long perfumeId = savedPerfume.getId();

        //when
        SaveReviewDto saveReviewDto = new SaveReviewDto("asdf", "content", userId, perfumeId, "impagePath");
        Long savedId = reviewService.save(saveReviewDto);


        assertThat(user.getReviews().size()).isEqualTo(1);
        assertThat(perfume.getReviews().size()).isEqualTo(1);
        System.out.println("1통과");

        SaveReviewDto newDto = new SaveReviewDto(savedId, "new subject", "new content", userId, perfumeId, "new image");
        Long newId = reviewService.update(newDto);

        //then
        assertThat(newId).isEqualTo(savedId);
        assertThat(user.getReviews().size()).isEqualTo(1);
        assertThat(perfume.getReviews().size()).isEqualTo(1);

    }

}