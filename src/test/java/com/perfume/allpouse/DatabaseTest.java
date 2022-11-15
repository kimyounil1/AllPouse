package com.perfume.allpouse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DatabaseTest {


    @Transactional
    @Rollback(value = false)
    @Test
    public void databaseTest() throws Exception{
        //given
        System.out.println("asdf");

        //when

        //then
    }


}
