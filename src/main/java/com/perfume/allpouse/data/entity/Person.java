package com.perfume.allpouse.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(name = "person_score")
    private BigDecimal score = BigDecimal.ZERO;


}
