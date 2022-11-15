package com.perfume.allpouse.data.entity;

import lombok.Getter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
public class Brand {

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "brand_id")
    private int id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String imagePath;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    List<PerfumeBoard> perfumes = new ArrayList<>();

}
