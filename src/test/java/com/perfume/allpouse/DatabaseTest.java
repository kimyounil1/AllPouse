package com.perfume.allpouse;

import com.perfume.allpouse.service.BrandService;
import com.perfume.allpouse.service.PerfumeService;
import com.perfume.allpouse.service.ReviewService;
import com.perfume.allpouse.service.UserService;
import com.perfume.allpouse.service.dto.SaveBrandDto;
import com.perfume.allpouse.service.dto.SavePerfumeDto;
import com.perfume.allpouse.service.dto.SaveReviewDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DatabaseTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @Transactional
    @Rollback(false)
    @Test
    public void databaseTest() throws Exception{

        reviewService.save(
                new SaveReviewDto(
                        "chanel_review", "good", 7L, 5L, "chanel_image_path"
                )
        );

    }


}
