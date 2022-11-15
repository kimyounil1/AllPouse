package com.perfume.allpouse.data.entity;
import com.perfume.allpouse.data.entity.enumDirectory.LoginType;
import com.perfume.allpouse.data.entity.enumDirectory.Permission;
import lombok.Getter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;

    private String socialId;

    private String userName;

    private int age;

    private String gender;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    private String userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ReviewBoard> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

}
