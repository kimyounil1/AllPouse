package com.perfume.allpouse.model.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInfoDto {
    private String userName;
    private int age;
    private String gender;
}
