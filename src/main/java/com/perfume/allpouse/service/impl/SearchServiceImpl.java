package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.repository.BrandRepository;
import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.model.dto.*;
import com.perfume.allpouse.service.SearchService;
import lombok.RequiredArgsConstructor;
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

        List<SearchReviewDto> reviews = reviewBoardRepository.search(keyword);

        List<SearchPostDto> posts = postRepository.search(keyword);

        return new SearchResultDto(brands, brands.size(), perfumes, perfumes.size(), reviews, reviews.size(), posts, posts.size());
    }
}
