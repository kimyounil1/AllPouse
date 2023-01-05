package com.perfume.allpouse.data.repository.Impl;


import com.perfume.allpouse.data.entity.PerfumerApplication;
import com.perfume.allpouse.data.repository.custom.PerfumerApplicationRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class PerfumerApplicationRepositoryImpl extends QuerydslRepositorySupport implements PerfumerApplicationRepositoryCustom {

    public PerfumerApplicationRepositoryImpl() {
        super(PerfumerApplication.class);
    }






}
