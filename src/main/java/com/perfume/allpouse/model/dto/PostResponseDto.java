package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.model.enums.BulletinType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponseDto {

    private Long id;

    private String type;

    private String title;

    private String content;

    private List<String> images;

    private int hitCnt;

    private int recommendCnt;

    private Long userId;

    private String userName;

    private List<String> userImage;

    private boolean isRecommended;

    private int commentCnt;

    private LocalDateTime createDateTime;


    @QueryProjection
    public PostResponseDto(Long id, BulletinType type, String title, String content, List<String> images, int hitCnt, int recommendCnt, Long userId, String userName, List<String> userImage, int commentCnt, LocalDateTime createDateTime) {
        this.id = id;
        this.type = type.getValue();
        this.title = title;
        this.content = content;
        this.images = images;
        this.hitCnt = hitCnt;
        this.recommendCnt = recommendCnt;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.commentCnt = commentCnt;
        this.createDateTime = createDateTime;
    }


    // 게시판 타입 설정
    public static List<PostResponseDto> setType(final List<PostResponseDto> postDtoList) {
        return null;
    }
}
