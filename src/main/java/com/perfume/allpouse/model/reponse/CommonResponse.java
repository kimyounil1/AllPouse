package com.perfume.allpouse.model.reponse;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
public class CommonResponse {

    private boolean success;
    private int code;
    private String msg;

}
