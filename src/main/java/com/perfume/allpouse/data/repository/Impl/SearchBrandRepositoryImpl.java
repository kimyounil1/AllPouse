package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.entity.QBrand;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.repository.search.SearchBrandRepository;
import com.perfume.allpouse.model.dto.QSearchBrandDto;
import com.perfume.allpouse.model.dto.SearchBrandDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.BRAND;

public class SearchBrandRepositoryImpl extends QuerydslRepositorySupport implements SearchBrandRepository {

    public SearchBrandRepositoryImpl() {
        super(Brand.class);
    }

    QBrand brand = QBrand.brand;
    QPhoto photo = QPhoto.photo;



    // 기본검색
    @Override
    public List<SearchBrandDto> search(String keyword) {

        List<SearchBrandDto> brands = from(brand)
                .leftJoin(photo)
                .on(brand.id.eq(photo.boardId).and(photo.boardType.eq(BRAND)))
                .where(brand.name.containsIgnoreCase(keyword))
                .select(new QSearchBrandDto(
                        brand.id,
                        brand.name,
                        photo.path))
                .fetch();

        return brands;
    }



}
