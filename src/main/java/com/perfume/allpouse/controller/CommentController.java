package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.data.entity.PostComment;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.CommentResponseDto;
import com.perfume.allpouse.model.dto.PostCommentResponseDto;
import com.perfume.allpouse.model.dto.SaveCommentDto;
import com.perfume.allpouse.model.dto.SavePostCommentDto;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.PageResponse;
import com.perfume.allpouse.service.*;
import com.perfume.allpouse.utils.PageUtils;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.perfume.allpouse.exception.ExceptionEnum.AUTHORITY_FORBIDDEN;
import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class CommentController {

    private final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    private final TokenProvider tokenProvider;

    private final CommentService commentService;

    private final ResponseService responseService;

    private final PostCommentService postCommentService;

    private final PostService postService;

    private final UserService userService;


    /**
     * 향수리뷰 댓글 API 메소드
     * 1. 댓글 저장 및 수정 - saveComment()
     * 2. 댓글 삭제 - deleteComment()
     * 3. 회원이 작성한 (향수리뷰) 댓글 가져옴 - myCommentList()
     * 4. 리뷰에 달린 댓글 가져옴 - commentsOnReview()
     */
    // 댓글 저장 및 수정
    // SaveCommentDto 필수 필드값 : (id), title, content, (reviewId) / 괄호는 수정 시
    @PostMapping(value = "comment")
    @Operation(summary = "향수 리뷰 댓글 저장 및 수정", description = "향수 리뷰에 대한 댓글을 저장하거나 수정하는 API")
    public CommonResponse saveAndUpdateComment (
            HttpServletRequest request,
            @ApiParam(value = "댓글 내용 담는 DTO", required = true) @RequestBody SaveCommentDto saveCommentDto) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

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
    @DeleteMapping(value = "comment")
    @Operation(summary = "향수 리뷰 댓글 삭제", description = "commentId로 리뷰에 대한 댓글을 삭제하는 API")
    public CommonResponse deleteComment(
            HttpServletRequest request,
            @ApiParam(value = "삭제하려는 댓글 id", required = true) @RequestParam Long commentId) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        Comment comment = commentService.findOne(commentId);

        Long commentUserId = comment.getUser().getId();

        // commentId로 찾은 댓글의할 작성자와 토큰으로 확인한 유저가 동일할 때에만 삭제 실행
        if (userId.equals(commentUserId)) {
            commentService.delete(commentId);
            return responseService.getSuccessCommonResponse();
        } else {throw new CustomException(INVALID_PARAMETER);}
    }


    // 회원이 작성한 댓글 페이지 별로 가져옴
    // 쿼리 파라미터(pageable에 매핑됨)로 페이지네이션 옵션 설정
    // 기본 설정 : 20 comments per Page, 최신순 정렬
    @GetMapping(value = "comment/me")
    @Operation(summary = "회원이 작성한 향수 리뷰 댓글", description = "회원이 작성한 향수 리뷰 댓글을 가져오는 API. 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음.")
    public PageResponse myCommentList(
            HttpServletRequest request,
            @ApiParam(value = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = DESC) Pageable pageable)
    {
        Long userId = tokenProvider.getUserIdFromRequest(request);


        Page<CommentResponseDto> pages = commentService.getUserCommentList(userId, pageable);

        return responseService.getPageResponse(pages);
    }


    // 최신 댓글 페이지 별로 가져옴
    // page/size만 쿼리파라미터로 받으면 됨(정렬은 기본설정 돼있음)
    // 다른 회원들한테 열어주면 안되고, ADMIN한테만 열어줘야함
    @GetMapping(value = "comment/recent")
    @Operation(summary = "최신 향수 리뷰 댓글", description = "최근에 작성된 댓글을 가져오는 API. 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음.")
    public PageResponse recentCommentList(
            @ApiParam(value = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = DESC) Pageable pageable)
    {
        Page<CommentResponseDto> pages = commentService.getAllCommentsList(pageable);

        return responseService.getPageResponse(pages);
    }


    // 리뷰에 달린 댓글들 페이지별로 가져옴
    // 기본옵션 : 20 comments per Page, 최신순 정렬
    @GetMapping(value = "comment/{reviewId}")
    @Operation(summary = "향수 리뷰에 달린 댓글", description = "해당 리뷰에 달린 댓글을 가져오는 API. 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음.")
    public PageResponse commentsOnReview(
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = DESC) Pageable pageable,
            @ApiParam(value = "리뷰 id", required = true) @PathVariable("reviewId") Long reviewId)
    {
        Page<CommentResponseDto> pages = commentService.getReviewCommentList(reviewId, pageable);

        return responseService.getPageResponse(pages);

    }



    /**
     * 게시글 댓글 API 메소드
     * 1. 게시글 댓글 작성 및 수정 - saveAndUpdatePostComment()
     * 2. 게시글 댓글 삭제 - deletePostComment()
     * 3. 회원이 작성한 게시글 댓글 - myPostCommentList()
     * 4. 게시글 댓글 추천 - recommendPostComment()
     */

    // 게시글 댓글 작성 및 수정
    @PostMapping("post-comment")
    @Operation(summary = "게시글 댓글 저장 및 수정", description = "게시글에 대한 댓글 저장, 수정하는 API")
    public CommonResponse saveAndUpdatePostComment(
            HttpServletRequest request,
            @ApiParam(value = "댓글 내용 담는 DTO", required = true) @RequestBody SavePostCommentDto commentDto) {

        String token = tokenProvider.resolveToken(request);

        Long userId = tokenProvider.getId(token);
        User user = userService.findOne(userId);
        commentDto.setUserId(userId);
        commentDto.setUserName(user.getUserName());

        Long postId = commentDto.getPostId();
        Post post = postService.findOne(postId);
        String type = post.getType().getValue();

        String role = tokenProvider.getRole(token);

        if (type.equals("자유게시판")) {
            postCommentService.save(commentDto);
        } else if (type.equals("조향사게시판")) {
            if (role.equals("ROLE_USER")) {
                throw new CustomException(AUTHORITY_FORBIDDEN);
            } else {
                postCommentService.save(commentDto);
            }
        }

        return responseService.getSuccessCommonResponse();
    }


    // 게시글 댓글 삭제
    @DeleteMapping(value = "post-comment")
    @Operation(summary = "게시글 댓글 삭제", description = "게시글 댓글 삭제하는 API")
    public CommonResponse deletePostComment(
            HttpServletRequest request,
            @ApiParam(value = "삭제하려는 댓글 Id", required = true) @RequestParam Long commentId) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        PostComment comment = postCommentService.findOne(commentId);

        Long commentUserId = comment.getUser().getId();

        if (userId.equals(commentUserId)) {
            postCommentService.delete(comment.getId());
            return responseService.getSuccessCommonResponse();
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
    }


    // 게시글에 달린 댓글 페이지 별로 가져옴
    // 쿼리 파라미터(pageable에 매핑됨)로 페이지네이션 옵션 설정
    // 기본 설정 : 20 comments per Page, 최신순 정렬
    @GetMapping(value="post-comment/{postId}")
    @Operation(summary="게시글에 달린 댓글", description="게시글 id로 해당 게시글에 달린 댓글 가져오는 API. 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음." +
            "<페이지네이션 설정 항목> : page, size, sort_direction(ASC, DESC). (sort 종류는 createDateTime으로 고정)")
    public PageResponse getPostComments(
            HttpServletRequest request,
            @ApiParam(value="게시글id", required = true) @PathVariable("postId") Long postId,
            @ApiParam(value="페이지네이션 옵션")
            @PageableDefault(page=0, size=20, sort="createDateTime", direction = DESC) Pageable pageable) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        Page<PostCommentResponseDto> pageList = postCommentService.getPostCommentPageList(postId, userId, pageable);

        return responseService.getPageResponse(pageList);
    }

    // 회원이 작성한 게시글 댓글 페이지 별로 가져옴
    // 쿼리 파라미터(pageable에 매핑됨)로 페이지네이션 옵션 설정
    // 기본 설정 : 20 comments per Page, 최신순 정렬
    @GetMapping(value = "post-comment/me")
    @Operation(summary = "회원이 작성한 게시글 댓글", description = "회원이 작성한 게시글 댓글을 가져오는 API. 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음.")
    public PageResponse myPostCommentList(
            HttpServletRequest request,
            @ApiParam(value = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = DESC) Pageable pageable) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        System.out.println("userId : " + userId);

        Page<PostCommentResponseDto> pages = postCommentService.getUserPostCommentList(userId, pageable);

        return responseService.getPageResponse(pages);
    }


    // 게시글 댓글 추천 API
    @PostMapping("post-comment/recommend/{commentId}")
    @Operation(summary = "게시글 댓글 추천 API", description = "게시글 댓글 추천 API" +
            "API 호출한 시점 기준으로 만약 이전에 추천한 적 없다면 추천하기 실행되고, 추천한 적있다면 추천 취소 실행")
    public CommonResponse recommendPostComment(
            HttpServletRequest request,
            @ApiParam(value = "게시글 댓글 id", required = true) @PathVariable("commentId") Long commentId) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        int index = postCommentService.updateRecommendCnt(commentId, userId);

        // 추천
        if (index == 0) {
            return new CommonResponse(true, 0, "추천완료");
        }
        // 추천 취소
        else {
            return new CommonResponse(true, 0, "추천취소완료");
        }
    }
}
