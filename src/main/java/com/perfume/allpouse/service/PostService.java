package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.model.dto.BannerResponseDto;
import com.perfume.allpouse.model.dto.PostResponseDto;
import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.model.enums.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    // 저장, 업데이트, 삭제
    Long save(SavePostDto savePostDto, Permission role);

    Long save(SavePostDto savePostDto, List<MultipartFile> photos, Permission role) throws IOException;

    Long update(SavePostDto savePostDto);

    void delete(Long postId);



    // 조회
    Post findOne(Long postId);

    List<PostResponseDto> getPopularPost(int size);

    PostResponseDto getPost(Long postId);

    Page<PostResponseDto> getUserPostList(Long userId, Pageable pageable);

    boolean isRecommended(Long postId, Long userId);

    Page<PostResponseDto> getPostByBoardType(Long boardId, Pageable pageable);

    List<BannerResponseDto> getBannerPost();




    // 기타 로직
    int updateRecommendCnt(Long postId, Long userId);

}
