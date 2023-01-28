package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.*;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.ListResponse;
import com.perfume.allpouse.model.reponse.PageResponse;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.PostCommentService;
import com.perfume.allpouse.service.PostService;
import com.perfume.allpouse.service.ResponseService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.model.enums.BoardType.POST;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/")
public class PostController {

    private final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    private final TokenProvider tokenProvider;

    private final PostService postService;

    private final PhotoService photoService;

    private final ResponseService responseService;

    private final PostCommentService postCommentService;


    // 게시글 저장 및 업데이트
    @PostMapping(value = "post", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "게시글 저장 및 수정", description = "게시글을 저장하거나 수정하는 API")
    public CommonResponse saveAndUpdatePost(
            HttpServletRequest request,
            @ApiParam(value = "게시글 내용을 담는 DTO", required = true) @RequestPart SavePostDto savePostDto,
            @ApiParam(value = "게시글에 첨부하는 사진들") @RequestPart(value = "photo", required = false) List<MultipartFile> photos) throws IOException {

        String token = tokenProvider.resolveToken(request);
        Long userId = tokenProvider.getId(token);
        Permission role = Permission.valueOf(tokenProvider.getRole(token));

        savePostDto.setUserId(userId);

        // 첨부사진 있는 경우
        if (photos != null) {
            postService.save(savePostDto, photos, role);
        }
        // 첨부사진 없는 경우
        else {
            postService.save(savePostDto, role);
        }
        return responseService.getSuccessCommonResponse();
    }


    // 게시글 삭제
    @DeleteMapping(value = "post")
    @Operation(summary = "게시글 삭제", description = "postId 받아서 해당 게시글 삭제하는 API")
    public CommonResponse deletePost(
            HttpServletRequest request,
            @ApiParam(value = "삭제하려는 게시글 id", required = true) @RequestParam Long postId) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        Post post = postService.findOne(postId);

        Long postUserId = post.getUser().getId();

        // 토큰으로 확인한 userId와 게시글 userId가 같을 때만 삭제
        if (userId.equals(postUserId)) {
            postService.delete(postId);
            photoService.delete(POST, postId);
            return responseService.getSuccessCommonResponse();
        } else {throw new CustomException(INVALID_PARAMETER);}
    }


    // 회원이 작성한 게시물 보기
    // 쿼리 파라미터(pageable에 매핑됨)로 페이지네이션 옵션 설정
    // 기본 설정 : 20 posts per Page, 최신순 정렬
    @GetMapping(value = "post/me")
    @Operation(summary = "회원이 쓴 게시물", description = "회원이 작성한 게시물 가져오는 API, 쿼리파라미터로 페이지네이션 옵션 지정할 수 있음")
    public PageResponse myPostList(
            HttpServletRequest request,
            @ApiParam(value = "페이지네이션 옵션")
            @PageableDefault(page = 0, size = 20, sort = "createDateTime", direction = DESC) Pageable pageable) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        Page<PostResponseDto> pages = postService.getUserPostList(userId, pageable);

        return responseService.getPageResponse(pages);
    }


    // 인기 게시글(N : 5)
    @GetMapping(value = "post/popular")
    @Operation(summary = "인기 게시글", description = "3개월 내 인기게시글 5개 가져오는 API")
    public ListResponse<PostResponseDto> popularPosts() {

        final int size = 5;

        List<PostResponseDto> postList = postService.getPopularPost(size);

        return responseService.getListResponse(postList);
    }


    // 게시글 상세 페이지
    // 게시글 1개 + 해당 게시글의 댓글 전체 전달
    @GetMapping(value = "post/{postId}")
    @Operation(summary = "게시글/댓글(전체)", description = "게시글과 해당 게시글에 달린 댓글(all)을 가져오는 API")
    public CommonResponse getPostPage (
            HttpServletRequest request,
            @ApiParam(value = "게시글 id", required = true) @PathVariable("postId") Long postId) {

        Long userId = tokenProvider.getUserIdFromRequest(request);
        boolean isRecommended;

        // 게시글
        PostResponseDto post = postService.getPost(postId);

        // 게시글 추천여부 -> Service 계층으로 옮기기
        post.setRecommended(postService.isRecommended(postId, userId));

        // 댓글
        List<PostCommentResponseDto> commentList = postCommentService.getPostCommentList(postId, userId);

        PostCommentDto postCommentDto = new PostCommentDto(post, commentList);

        return responseService.getSingleResponse(postCommentDto);
    }


    // 게시글 추천 API
    @PostMapping(value = "post/recommend/{postId}")
    @Operation(summary = "게시글 추천", description = "게시글 추천 API. " +
            "API 호출한 시점 기준으로 만약 이전에 추천한 적 없다면 추천하기 실행되고, 추천한 적있다면 추천 취소 실행")
    public CommonResponse recommendPost(
            HttpServletRequest request,
            @ApiParam(value = "게시글 id", required = true) @PathVariable Long postId) {

        Long userId = tokenProvider.getUserIdFromRequest(request);

        int index = postService.updateRecommendCnt(postId, userId);

        // 추천
        if (index == 0) {
            return new CommonResponse(true, 0, "추천완료");
        }
        // 추천 취소
        else {
            return new CommonResponse(true, 0, "추천취소완료");
        }
    }


    // 배너 게시글 가져옴
    @GetMapping(value = "banner")
    @Operation(summary = "배너 게시글", description = "배너 게시글 가져오는 API. 데이터 : 최신순으로 정렬되어 있음")
    public ListResponse<BannerResponseDto> getBannerPost() {

        List<BannerResponseDto> banners = postService.getBannerPost();
        return responseService.getListResponse(banners);
    }

}
