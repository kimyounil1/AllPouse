package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.FreeBoardPost;
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


    @Override
    public Long save(SavePostDto savePostDto) {
        return null;
    }

    @Override
    public Long save(SavePostDto savePostDto, List<MultipartFile> photos) throws IOException {
        return null;
    }

    @Override
    public Long update(SavePostDto savePostDto) {
        return null;
    }
}
