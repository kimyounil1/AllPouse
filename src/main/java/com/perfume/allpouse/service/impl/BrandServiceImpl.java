package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.entity.QBrand;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.repository.BrandRepository;
import com.perfume.allpouse.model.dto.BrandInfoDto;
import com.perfume.allpouse.model.dto.QBrandInfoDto;
import com.perfume.allpouse.model.dto.SaveBrandDto;
import com.perfume.allpouse.model.enums.BoardType;
import com.perfume.allpouse.service.BrandService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.perfume.allpouse.model.enums.BoardType.*;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    private final JPAQueryFactory queryFactory;

    QBrand brand = new QBrand("brand");

    QPhoto photo = new QPhoto("photo");


    // 브랜드 저장
    @Transactional
    @Override
    public Long save(SaveBrandDto saveBrandDto) {

        Brand savedBrand = brandRepository.save(toEntity(saveBrandDto));

        return savedBrand.getId();
    }


    // 브랜드 수정
    @Transactional
    @Override
    public Long update(SaveBrandDto saveBrandDto) {
        Brand brand = brandRepository.findById(saveBrandDto.getId()).get();
        brand.changeBrand(saveBrandDto);

        return brand.getId();
    }


    // 브랜드 삭제(By id)
    @Transactional
    @Override
    public void delete(Long id) {

        Optional<Brand> brand = brandRepository.findById(id);

        if (brand.isPresent()) {
            brandRepository.deleteById(id);
        } else {
            throw new IllegalStateException("삭제할 브랜드가 없습니다.");
        }
    }


    // 전체 브랜드 조회
    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }


    // 브랜드 단건 조회(with id)
    @Override
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
    @Override
    public List<Brand> find(String name) {
        List<Brand> brands = brandRepository.findByNameContainingOrderByNameAsc(name);

        if (brands.isEmpty()) {
            throw new IllegalStateException("브랜드를 찾을 수 없습니다.");
        } else {
            return brands;
        }
    }


    // 브랜드 Id로 BrandInfoDto 받는 메소드
    @Override
    public BrandInfoDto getBrandInfo(Long id) {
        BrandInfoDto brandInfoDto = queryFactory
                .select(new QBrandInfoDto(brand.id, brand.name, brand.content, photo.path))
                .from(brand)
                .leftJoin(photo)
                .on(brand.id.eq(photo.boardId).and(photo.boardType.eq(BRAND)))
                .where(brand.id.eq(id))
                .fetchOne();

        return brandInfoDto;
    }


    private Brand toEntity(SaveBrandDto brandDto) {

        return Brand.builder()
                .id(brandDto.getId())
                .name(brandDto.getName())
                .content(brandDto.getContent())
                .build();
    }
}
