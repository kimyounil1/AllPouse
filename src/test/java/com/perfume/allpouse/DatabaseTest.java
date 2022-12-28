package com.perfume.allpouse;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.model.dto.SaveBrandDto;
import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.service.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

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

    @Autowired
    PostService postService;

    @Test
    @DisplayName("추천 테스트")
    @Transactional
    public void recommendTest() throws Exception {
        //given
        Post post = postService.findOne(60L);
        List<Long> userList = post.getRecommendUserList();
        int result_1 = postService.updateRecommendCnt(60L, 5L);
        postService.updateRecommendCnt(post.getId(), 3L);


        assertThat(result_1).isEqualTo(0);

        //when
        int result_2 = postService.updateRecommendCnt(60L, 5L);
        assertThat(result_2).isEqualTo(1);


        //then

    }

}
