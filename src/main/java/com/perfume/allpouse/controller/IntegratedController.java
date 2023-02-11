package com.perfume.allpouse.controller;


import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.service.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 여러 엔티티들에 대한 클라이언트 요청 통합적으로 처리하는 컨트롤러
 * <p>
 * <API METHOD LIST>
 * 1. getDataByPeriod
 * : Review/Comment(Review)/Post/PostComment 기간별로 가져오는 API 메소드
 * - 기간 : 1주, 1개월, 6개월, 1년
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
public class IntegratedController {

    private final Logger LOGGER = LoggerFactory.getLogger(PerfumeController.class);

    private final TokenProvider tokenProvider;

    private final ReviewService reviewService;

    private final CommentService commentService;

    private final PostService postService;

    private final PostCommentService postCommentService;

    private final PhotoService photoService;

    private final ResponseService responseService;


}
