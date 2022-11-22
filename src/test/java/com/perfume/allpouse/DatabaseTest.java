package com.perfume.allpouse;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.model.dto.SaveBrandDto;
import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.service.BrandService;
import com.perfume.allpouse.service.PerfumeService;
import com.perfume.allpouse.service.ReviewService;
import com.perfume.allpouse.service.UserService;
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

    @Autowired
    BrandService brandService;

    @Autowired
    PerfumeService perfumeService;

    @Transactional
    @Rollback(false)
    @Test
    public void databaseTest() throws Exception{


    }



    private void savePerfume(String subject, String content, int price, Long brandId, String imagePath) {
        perfumeService.save(
                new SavePerfumeDto(subject, content, price, brandId, imagePath)
        );
    }


    private void saveBrand(String name, String content, String imagePath) {
        brandService.save(
                new SaveBrandDto(name, content, imagePath)
        );
    }


}
