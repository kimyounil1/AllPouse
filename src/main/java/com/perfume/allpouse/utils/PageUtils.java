package com.perfume.allpouse.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtils {

    public static <T> Page<T> makePageList(List<T> dataList, Pageable pageable) {

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dataList.size());

        Page<T> pages = new PageImpl<>(dataList.subList(start, end), pageable, dataList.size());

        return pages;
    }
}
