package com.perfume.allpouse.data.repository;


import com.perfume.allpouse.data.entity.PerfumerApplication;
import com.perfume.allpouse.data.repository.custom.PerfumerApplicationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumerApplicationRepository extends JpaRepository<PerfumerApplication, Long>, PerfumerApplicationRepositoryCustom {


}

