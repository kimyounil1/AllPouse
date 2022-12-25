package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SavePostDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.ConstraintMode.*;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.*;
import static lombok.AccessLevel.PROTECTED;

/**
 * 게시판의 게시글 상위 클래스
 */

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name = "type")
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "post_id")
    private Long id;

    @ColumnDefault("0")
    private int hitCnt;

    @ColumnDefault("0")
    private int recommendCnt;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<PostComment> postComments = new ArrayList<>();


    public Post(String title, String content, User user) {
        this.hitCnt = hitCnt;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    //== 연관관계 메소드 ==//
    // 1. User
    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }


    //== 게시글 내용 변경 ==//
    // 1. Dto -> Entity 내용변경
    // 변경가능항목 : title, content
    public void changePost(SavePostDto postDto) {
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
    }
}
