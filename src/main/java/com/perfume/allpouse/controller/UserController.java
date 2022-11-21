package com.perfume.allpouse.controller;

import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.service.impl.ResponseServiceImpl;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserController {

    ResponseServiceImpl responseService;
    @GetMapping(value = "user-info")
    public CommonResponse userInfo() {
        Jwts.parserBuilder().build();
        CommonResponse commonResponse = new CommonResponse();
        responseService.setSuccessResponse(commonResponse);
        return commonResponse;
    }
}
