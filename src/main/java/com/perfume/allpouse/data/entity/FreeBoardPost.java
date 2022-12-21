package com.perfume.allpouse.data.entity;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import static lombok.AccessLevel.*;

@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@Getter
@DiscriminatorValue("free")
public class FreeBoardPost extends Post{

}
