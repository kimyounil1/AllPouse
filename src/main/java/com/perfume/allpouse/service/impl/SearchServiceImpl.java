package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.repository.BrandRepository;
import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.model.dto.*;
import com.perfume.allpouse.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final BrandRepository brandRepository;

    private final ReviewBoardRepository reviewBoardRepository;

    private final PerfumeBoardRepository perfumeBoardRepository;

    private final PostRepository postRepository;


    // 기본검색(with 키워드)
    @Override
    public SearchResultDto searchWithKeyword(String keyword) {

        List<SearchBrandDto> brands = brandRepository.search(keyword);

        List<SearchPerfumeDto> perfumes = perfumeBoardRepository.search(keyword);

        List<ReviewResponseDto> reviews = reviewBoardRepository.search(keyword);

        List<PostResponseDto> posts = postRepository.search(keyword);

        return new SearchResultDto(brands, brands.size(), perfumes, perfumes.size(), reviews, reviews.size(), posts, posts.size());
    }


    // 게시글 기본검색(with 키워드) + 페이지네이션
    @Override
    public Page<PostResponseDto> searchPostWithKeyword(String keyword, Pageable pageable) {

        return postRepository.searchWithPaging(keyword, pageable);
    }


    // 리뷰 기본검색(with 키워드) + 페이지네이션
    @Override
    public Page<ReviewResponseDto> searchReviewWithKeyword(String keyword, Pageable pageable) {

        return reviewBoardRepository.searchWithPaging(keyword, pageable);
    }
}
