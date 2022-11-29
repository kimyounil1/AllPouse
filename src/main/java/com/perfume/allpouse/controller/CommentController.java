package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.CommentResponseDto;
import com.perfume.allpouse.model.dto.SaveCommentDto;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.service.CommentService;
import com.perfume.allpouse.service.ResponseService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")

public class CommentController {

    private final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    private final TokenProvider tokenProvider;

    private final CommentService commentService;

    private final ResponseService responseService;


    // 댓글 저장 및 수정
    @ResponseBody
    @PostMapping("comment")
    public CommonResponse saveComment (
            HttpServletRequest request,
            @ApiParam(value = "댓글 내용 담는 DTO", required = false) @RequestBody SaveCommentDto saveCommentDto) {

        Long userId = getUserIdFromRequest(request);

        saveCommentDto.setUserId(userId);
        Long reviewId = saveCommentDto.getId();

        if (reviewId == null) {
            commentService.save(saveCommentDto);

            //response
            CommonResponse response = new CommonResponse();
            responseService.setSuccessResponse(response);
            return response;
        } else {
            if (userId.equals(saveCommentDto.getUserId())) {
                commentService.update(saveCommentDto);



                //response
                CommonResponse response = new CommonResponse();
                responseService.setSuccessResponse(response);
                return response;

            } else {
                throw new CustomException(INVALID_PARAMETER);
            }
        }
    }


    // 댓글 삭제
    @ResponseBody
    @DeleteMapping("comment")
    public CommonResponse deleteComment(
            HttpServletRequest request,
            @ApiParam(value = "삭제하려는 댓글 id", required = true) @RequestParam Long commentId) {

        Long userId = getUserIdFromRequest(request);

        Comment comment = commentService.findOne(commentId);

        Long commentUserId = comment.getUser().getId();

        if (userId.equals(commentUserId)) {
            commentService.delete(commentId);

            // response
            CommonResponse response = new CommonResponse();
            responseService.setSuccessResponse(response);
            return response;
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
    }



    // 회원이 쓴 댓글 페이지 별로 가져옴
    // 쿼리 파라미터(pageable에 매핑됨)로 페이지네이션 옵션 설정
    @ResponseBody
    @GetMapping("comment/me")
    public Page<CommentResponseDto> myCommentList(
            HttpServletRequest request,
            @ApiParam(value = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 20) Pageable pageable)
    {
        Long userId = getUserIdFromRequest(request);

        return commentService.getUserReviewList(userId, pageable);
    }





    // HttpServletRequest에서 userId 추출하는 메소드
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);

        return tokenProvider.getId(token);
    }


}
