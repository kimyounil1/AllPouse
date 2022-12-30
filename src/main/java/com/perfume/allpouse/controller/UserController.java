package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.model.dto.UserInfoDto;
import com.perfume.allpouse.model.enums.BoardType;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.impl.PhotoServiceImpl;
import com.perfume.allpouse.service.impl.ResponseServiceImpl;
import com.perfume.allpouse.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final TokenProvider tokenProvider;
    private final ResponseServiceImpl responseServiceImpl;
    private final PhotoServiceImpl photoServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public UserController(TokenProvider tokenProvider, ResponseServiceImpl responseServiceImpl, PhotoServiceImpl photoServiceImpl, UserServiceImpl userServiceImpl) {
        this.tokenProvider = tokenProvider;
        this.responseServiceImpl = responseServiceImpl;
        this.photoServiceImpl = photoServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping(value = "user/user-info")
    public SingleResponse<UserInfoDto> userInfo(HttpServletRequest request) {
        LOGGER.info("[userInfo] user 정보 불러오기 ");
        long id = tokenProvider.getId(tokenProvider.resolveToken(request));
        UserInfoDto user;
        if (!photoServiceImpl.getExistsImage(BoardType.USER,id))
        {
            user = userServiceImpl.loadUserById(id).toUserInfoDto();
        }
        else {
            user = userServiceImpl.getUserInfoDtoById(id);
        }
        return responseServiceImpl.getSingleResponse(user);
    }

    @ResponseBody
    @PostMapping(value = "user/photo", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public SingleResponse<String> uploadUserPhoto(HttpServletRequest request,
    @ApiParam(value = "유저 사진") @RequestPart(value = "photo", required = false) List<MultipartFile> photos) throws IOException {
        LOGGER.info("[uploadUserPhoto] user 정보 불러오기 ");
        long id = tokenProvider.getId(tokenProvider.resolveToken(request));
        LOGGER.info("[uploadUserPhoto] 새로운 Photo 정보 업데이트 ");
        List<String> fileNameList = photoServiceImpl.update(photos, BoardType.USER, id);
        return responseServiceImpl.getSingleResponse(fileNameList.get(0));
    }

    @ResponseBody
    @DeleteMapping (value = "user/photo")
    public CommonResponse uploadUserPhoto(HttpServletRequest request) throws IOException {
        LOGGER.info("[uploadUserPhoto] user 정보 불러오기 ");
        long id = tokenProvider.getId(tokenProvider.resolveToken(request));

        LOGGER.info("[uploadUserPhoto] Photo 삭제");
        photoServiceImpl.delete( BoardType.USER, id);

        CommonResponse response = new CommonResponse();
        responseServiceImpl.setSuccessResponse(response);
        return response;
    }
}
