package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class PerfumeController {

    private final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    private final TokenProvider tokenProvider;

    private final PerfumeService perfumeService;


    // 향수 페이지
    @ResponseBody
    @GetMapping("perfume")
    public getPerfumePage() {


    }

    // 향수 추가

    // 향수 삭제



}
