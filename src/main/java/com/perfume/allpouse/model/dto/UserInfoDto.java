package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.utils.StringListConverter;
import lombok.*;
import javax.persistence.Convert;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String userName;
    private int age;
    private String gender;
    private List<String> image;
}
