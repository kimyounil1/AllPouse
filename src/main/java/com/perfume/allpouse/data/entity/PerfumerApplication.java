package com.perfume.allpouse.data.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class PerfumerApplication extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "perfumer_application_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 신청하면서 운영진에게 남기고 싶은 말
    private String text;

    // 승인여부(false : 신청상태 / true : 승인상태)
    private boolean isApproved;


    //== 비니지스 로직 관련 메소드 ==//
    public void changeApproval(boolean status){
        this.isApproved = status;
    }
}
