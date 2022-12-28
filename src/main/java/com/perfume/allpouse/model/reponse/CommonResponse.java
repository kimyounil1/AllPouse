package com.perfume.allpouse.model.reponse;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {

    private boolean success;
    private int code;
    private String msg;
}
