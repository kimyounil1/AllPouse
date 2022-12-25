package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.FreeBoardPost;
import com.perfume.allpouse.data.entity.PerfumerBoardPost;
import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.model.enums.BoardType.POST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    private final PhotoService photoService;

    private final UserRepository userRepository;


    // 게시글 저장(사진X)
    @Override
    public Long save(SavePostDto savePostDto) {

        Long postId = savePostDto.getId();

        // 등록된 적 없는 글 -> save
        if (postId == null) {

            User user = userRepository.findById(savePostDto.getUserId()).get();
            Long savedPostId = checkBulletinType(savePostDto, user);
            return savedPostId;
        }

        // 등록된 적 있는 글 -> 사진 삭제, 게시글 내용 변경
        else {
            photoService.delete(POST, postId);
            update(savePostDto);
            return postId;
        }
    }


    // 게시글 저장(사진O)
    @Override
    public Long save(SavePostDto savePostDto, List<MultipartFile> photos) throws IOException {

        Long postId = savePostDto.getId();

        // 등록된 적 없는 글 -> 글/사진 저장
        if (postId == null) {
            Long savedId = save(savePostDto);
            photoService.save(photos, POST, savedId);
            return savedId;
        }

        // 등록된 적 있는 글 -> 사진 삭제, 게시글 내용 변경
        else {
            photoService.delete(POST, postId);
            photoService.save(photos, POST, postId);
            update(savePostDto);
            return postId;
        }
    }


    // 게시글 삭제
    @Override
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }


    // postId로 게시글 검색
    @Override
    public Post findById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {
            throw new CustomException(INVALID_PARAMETER);
        } else {
            return post.get();
        }
    }


    // 게시글 업데이트
    @Override
    public Long update(SavePostDto savePostDto) {

        Long postId = savePostDto.getId();

        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent()) {
            Post findPost = post.get();
            findPost.changePost(savePostDto);

            return postId;
        } else {throw new CustomException(INVALID_PARAMETER);}

    }


    // 게시판 타입 체크해서 분류 후 저장
    private Long checkBulletinType(SavePostDto savePostDto, User user) {

        switch (savePostDto.getType()) {
            case "free":
                FreeBoardPost freeBoardPost = FreeBoardPost.builder()
                        .title(savePostDto.getTitle())
                        .content(savePostDto.getContent())
                        .user(user)
                        .build();

                return postRepository.save(freeBoardPost).getId();

            case "perfumer":
                PerfumerBoardPost perfumerBoardPost = PerfumerBoardPost.builder()
                        .title(savePostDto.getTitle())
                        .content(savePostDto.getContent())
                        .user(user)
                        .build();

                return postRepository.save(perfumerBoardPost).getId();

            // 게시판 설정 안하거나, 없는 게시판 설정
            default:
                throw new CustomException(INVALID_PARAMETER);
        }
    }
}
