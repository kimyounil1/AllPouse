package com.perfume.allpouse.service;


import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.repository.BrandRepository;
import com.perfume.allpouse.service.dto.SaveBrandDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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


    // 브랜드 삭제(By id)
    @Transactional
    public void delete(Long id) {
        brandRepository.deleteById(id);
    }


    // 브랜드 전체 조회
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }


    // 브랜드 단건 조회(with id)
    public Brand findOne(Long id) {
        Optional<Brand> findBrand = brandRepository.findById(id);

        if(findBrand.isPresent()) {
            return findBrand.get();
        } else {
            throw new IllegalStateException("브랜드를 찾을 수 없습니다.");
        }
    }


    // 브랜드 조회(파라미터 name을 포함한 브랜드 검색)
    // 기본정렬 : 이름순 오름차순
    public List<Brand> find(String name) {
        List<Brand> brands = brandRepository.findByNameContainingOrderByNameAsc(name);

        if(brands.isEmpty()) {
            throw new IllegalStateException("브랜드를 찾을 수 없습니다.");
        } else {
            return brands;
        }
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
