package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.model.dto.SearchResultDto;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.ResponseService;
import com.perfume.allpouse.service.SearchService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/")
public class SearchController {

    private final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    private final SearchService searchService;

    private final ResponseService responseService;

    private final TokenProvider tokenProvider;


    @GetMapping("search/{keyword}")
    @Operation(summary = "기본검색 - 키워드로 검색", description = "기본검색 - 키워드를 입력하면 키워드를 이름/내용에 포함하는 브랜드(all), 향수(all), 리뷰(10), 게시글(10) 가져옴")
    public SingleResponse<SearchResultDto> basicSearch(@ApiParam(value = "검색어", required = true) @PathVariable("keyword") String keyword) {

        SearchResultDto result = searchService.searchWithKeyword(keyword);

        return responseService.getSingleResponse(result);
    }
}
