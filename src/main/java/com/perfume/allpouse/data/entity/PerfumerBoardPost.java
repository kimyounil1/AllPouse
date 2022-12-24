package com.perfume.allpouse.data.entity;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import static lombok.AccessLevel.*;

@Entity
@NoArgsConstructor
@DiscriminatorValue("perfumer")
public class PerfumerBoardPost extends Post{

    @Builder
    public PerfumerBoardPost(String title, String content, User user) {
        super(title, content, user);
    }
}
