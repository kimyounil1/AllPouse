package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.Post;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class SavePostDto {

    private Long id;

    private String title;

    private String content;

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
