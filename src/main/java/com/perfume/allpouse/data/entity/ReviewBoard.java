package com.perfume.allpouse.data.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
public class ReviewBoard extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "review_id")
    private int id;

    private int hitCnt;

    private int recommendCnt;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_board_id")
    private PerfumeBoard perfumeBoard;

    private String imagePath;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();

}
