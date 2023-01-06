package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.repository.PerfumerApplicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PerfumerApplicationRepositoryImplTest {

    @Autowired
    PerfumerApplicationRepository repository;

    @Test
    public void 삭제테스트() throws Exception{
        //given
        repository.delete(17L);

        //when

        //then

    }

}