package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.model.dto.PostResponseDto;
import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.model.enums.Permission;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Long save(SavePostDto savePostDto, Permission role);

    Long save(SavePostDto savePostDto, List<MultipartFile> photos, Permission role) throws IOException;

    Long update(SavePostDto savePostDto);

    void delete(Long postId);

    Post findOne(Long postId);

    List<PostResponseDto> getPopularPost(int size);





}
