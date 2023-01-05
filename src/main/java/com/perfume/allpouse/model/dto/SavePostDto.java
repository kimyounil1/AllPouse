package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class SavePostDto {

    private Long id;

    private String title;

    private String content;

    // 게시판 타입
    private String type;

    private Long userId;

    // Post -> Dto
    public SavePostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userId = post.getUser().getId();
    }
}
