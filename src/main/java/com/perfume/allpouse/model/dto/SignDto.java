package com.perfume.allpouse.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
public class SignDto {

    private String accessToken;

    private String refreshToken;

}