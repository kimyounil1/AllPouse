package com.perfume.allpouse.model.reponse;

import lombok.*;

import java.util.List;
@Data
public class ListResponse<T> extends CommonResponse {
    private List<T> dataList;
    private int count;
}
