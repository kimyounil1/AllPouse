package com.perfume.allpouse.model.reponse;

import lombok.Data;


@Data
public class CommonResponse {

    private boolean success;
    private String code = "";
    private String msg;

}
