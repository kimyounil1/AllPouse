package com.perfume.allpouse.data.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("free")
public class FreeBoardPost extends Post{

    @Builder
    public FreeBoardPost(String title, String content, User user) {
        super(title, content, user);
    }

}
