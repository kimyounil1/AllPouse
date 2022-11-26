package com.perfume.allpouse.data.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "photo")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String type;

    private String path;

    @Column(name="board_id")
    private Long boardId;

}
