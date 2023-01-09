package com.perfume.allpouse.data;

import org.junit.jupiter.api.Test;

public class PlainTest {

    @Test
    public void parsingTest() throws Exception {
        //given
        String url = "https://perfume-log.s3.ap-northeast-2.amazonaws.com/8962df72-6da0-48a2-9ab0-39533afaa585-IMG_5765734896122891553.webp";
        String amazonUrl = "https://perfume-log.s3.ap-northeast-2.amazonaws.com/";
        int start = amazonUrl.length();
        int end = url.length();

        String substring = url.substring(start, end);

        System.out.println(substring);
    }
}
