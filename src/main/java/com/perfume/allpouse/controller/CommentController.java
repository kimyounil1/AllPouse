package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.CommentResponseDto;
import com.perfume.allpouse.model.dto.SaveCommentDto;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.PageResponse;
import com.perfume.allpouse.service.CommentService;
import com.perfume.allpouse.service.ResponseService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    // SaveCommentDto 필수 필드값 : (id), title, content, (reviewId) / 괄호는 수정 시
    @ResponseBody
    @PostMapping("comment")
    @Operation(summary = "댓글 저장 및 수정", description = "리뷰에 대한 댓글을 저장하거나 수정하는 API")
    public CommonResponse saveComment (
            HttpServletRequest request,
            @ApiParam(value = "댓글 내용 담는 DTO", required = false) @RequestBody SaveCommentDto saveCommentDto) {

        Long userId = getUserIdFromRequest(request);

        saveCommentDto.setUserId(userId);
        Long reviewId = saveCommentDto.getId();

        // reviewId 없으면 저장
        if (reviewId == null) {
            commentService.save(saveCommentDto);

            //response
            return responseService.getSuccessCommonResponse(); }

        // reviewId 있으면 수정
        else {
            if (userId.equals(saveCommentDto.getUserId())) {
                commentService.update(saveCommentDto);
                return responseService.getSuccessCommonResponse();
            } else {throw new CustomException(INVALID_PARAMETER); }
        }
    }


    // 댓글 삭제
    @ResponseBody
    @DeleteMapping("comment")
    @Operation(summary = "댓글 삭제", description = "commentId로 리뷰에 대한 댓글을 삭제하는 API")
    public CommonResponse deleteComment(
            HttpServletRequest request,
            @ApiParam(value = "삭제하려는 댓글 id", required = true) @RequestParam Long commentId) {

        Long userId = getUserIdFromRequest(request);

        Comment comment = commentService.findOne(commentId);

        Long commentUserId = comment.getUser().getId();

        // commentId로 찾은 댓글의할 작성자와 토큰으로 확인한 유저가 동일할 때에만 삭제 실행
        if (userId.equals(commentUserId)) {
            commentService.delete(commentId);
            return responseService.getSuccessCommonResponse();
        } else {throw new CustomException(INVALID_PARAMETER);}
    }


    // 회원이 쓴 댓글 페이지 별로 가져옴
    // 쿼리 파라미터(pageable에 매핑됨)로 페이지네이션 옵션 설정
    // 기본 설정 : 20 comments per Page, 최신순 정렬
    @ResponseBody
    @GetMapping("comment/me")
    @Operation(summary = "회원이 쓴 댓글", description = "회원이 작성한 댓글을 가져오는 API. 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음.")
    public PageResponse myCommentList(
            HttpServletRequest request,
            @ApiParam(value = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Long userId = getUserIdFromRequest(request);

        Page<CommentResponseDto> pages = commentService.getUserCommentList(userId, pageable);

        return responseService.getPageResponse(pages);
    }


    // 최신 댓글 페이지 별로 가져옴
    // page/size만 쿼리파라미터로 받으면 됨(정렬은 기본설정 돼있음)
    // 다른 회원들한테 열어주면 안되고, ADMIN한테만 열어줘야함
    @ResponseBody
    @GetMapping("comment/recent")
    @Operation(summary = "최신 댓글", description = "최근에 작성된 댓글을 가져오는 API. 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음.")
    public PageResponse recentCommentList(
            @ApiParam(value = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<CommentResponseDto> pages = commentService.getAllCommentsList(pageable);

        return responseService.getPageResponse(pages);
    }


    // 리뷰에 달린 댓글들 페이지별로 가져옴
    // 기본옵션 : 20 comments per Page, 최신순 정렬
    @ResponseBody
    @GetMapping("comment/{reviewId}")
    @Operation(summary = "리뷰에 달린 댓글", description = "해당 리뷰에 달린 댓글을 가져오는 API. 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음.")
    public PageResponse commentsOnReview(
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = Sort.Direction.DESC) Pageable pageable,
            @ApiParam(value = "리뷰 id", required = true) @PathVariable("reviewId") Long reviewId)
    {
        Page<CommentResponseDto> pages = commentService.getReviewCommentList(reviewId, pageable);

        return responseService.getPageResponse(pages);

    }


    // HttpRequest에서 userId 추출
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);

        return tokenProvider.getId(token);
    }
}
