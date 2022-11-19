package com.perfume.allpouse.model.reponse;

import lombok.Data;

@Data
public class SingleResponse<T> extends CommonResponse{

    T data;

}
