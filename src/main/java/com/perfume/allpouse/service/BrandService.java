package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.model.dto.BrandInfoDto;
import com.perfume.allpouse.model.dto.SaveBrandDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BrandService {

    Long save(SaveBrandDto saveBrandDto, List<MultipartFile> photos) throws IOException;

    Long save(SaveBrandDto saveBrandDto);

    Long update(SaveBrandDto saveBrandDto);

    void delete(Long id);


    List<Brand> findAll();

    Brand findOne(Long id);

    List<Brand> find(String name);

    BrandInfoDto getBrandInfo(Long id);
}
