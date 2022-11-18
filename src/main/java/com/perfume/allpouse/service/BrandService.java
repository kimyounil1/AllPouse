package com.perfume.allpouse.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BrandService {


    // 브랜드 저장
    @Transactional
    public Long save() {

    }




}
