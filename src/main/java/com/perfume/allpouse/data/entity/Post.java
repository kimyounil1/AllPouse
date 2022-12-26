package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.model.enums.BulletinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;

/**
 * 게시판의 게시글 상위 클래스
 */

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(value = STRING)
    private BulletinType type;

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
    private List<PostComment> postComments = new ArrayList<>();


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
