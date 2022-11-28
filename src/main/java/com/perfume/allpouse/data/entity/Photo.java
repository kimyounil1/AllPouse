package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.enums.BoardType;
import com.perfume.allpouse.utils.StringListConverter;
import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.EnumType.*;
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

    @Enumerated(value = STRING)
    private BoardType boardType;

    @Convert(converter = StringListConverter.class)
    private List<String> path;

    @Column(name="board_id")
    private Long boardId;

}
