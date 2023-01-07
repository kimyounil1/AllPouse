package com.perfume.allpouse.data.repository.Impl;


import com.perfume.allpouse.data.entity.PerfumerApplication;
import com.perfume.allpouse.data.repository.custom.PerfumerApplicationRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class PerfumerApplicationRepositoryImpl extends QuerydslRepositorySupport implements PerfumerApplicationRepositoryCustom {


    public PerfumerApplicationRepositoryImpl() {
        super(PerfumerApplication.class);
    }





}
