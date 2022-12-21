package com.perfume.allpouse.data.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "brand_log")
public class BoardLog {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private Boolean status;

    private String type;

    private String action;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long boardId;

    private String detailLog;
}
