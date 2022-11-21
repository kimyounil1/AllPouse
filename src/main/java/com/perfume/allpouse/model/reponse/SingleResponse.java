package com.perfume.allpouse.model.reponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class SingleResponse<T> extends CommonResponse{

    T data;

}
