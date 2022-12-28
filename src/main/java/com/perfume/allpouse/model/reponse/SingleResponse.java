package com.perfume.allpouse.model.reponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SingleResponse<T> extends CommonResponse{

    T data;

}
