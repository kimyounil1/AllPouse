package com.perfume.allpouse.service;


import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.repository.BrandRepository;
import com.perfume.allpouse.service.dto.SaveBrandDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;


    // 브랜드 저장
    @Transactional
    public Long save(SaveBrandDto saveBrandDto) {

        Brand savedBrand = brandRepository.save(toEntity(saveBrandDto));

        return savedBrand.getId();
    }

    // 브랜드 수정
    @Transactional
    public Long update(SaveBrandDto saveBrandDto) {
        Brand brand = brandRepository.findById(saveBrandDto.getId()).get();
        brand.changeBrand(saveBrandDto);

        return brand.getId();
    }



    private Brand toEntity(SaveBrandDto brandDto) {

        return Brand.builder()
                .id(brandDto.getId())
                .name(brandDto.getName())
                .content(brandDto.getContent())
                .imagePath(brandDto.getImagePath())
                .build();
    }
}
