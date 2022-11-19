package com.perfume.allpouse.model.reponse;

import lombok.*;

import java.util.List;
@Data
public class ListResponse<T> extends CommonResponse {
    List<T> dataList;
}
