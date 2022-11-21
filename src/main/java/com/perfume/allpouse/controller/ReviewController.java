package com.perfume.allpouse.controller;

import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.model.reponse.CommonResponse;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @PostMapping(value = "/")
    public CommonResponse saveReview(
            @ApiParam(value = "subject", required = true) @RequestParam String subject,
            @ApiParam(value = "content", required = true) @RequestParam String content,
            @ApiParam(value = "userId", required = true) @RequestParam Long userId,
            @APiParam()){


        CommonResponse response = new CommonResponse();


    }




    // review 작성


    // review 수정

    // review 삭제

    // review 조회 -> UserController에서
}
