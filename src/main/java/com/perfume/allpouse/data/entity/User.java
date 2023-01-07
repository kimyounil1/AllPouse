package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.UserInfoDto;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.utils.StringListConverter;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "users")
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String socialId;

    private String userName;

    private int age;

    private String gender;

    @Enumerated(EnumType.STRING)
    private Permission permission;

    private String loginType;

    private String userStatus;

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<ReviewBoard> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    @Builder.Default
    private List<PostComment> postComments = new ArrayList<>();


    //== 비지니스 로직 관련 메소드 ==//
    public void changePermission(Permission permission) {
        this.permission = permission;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(getPermission().getValue()));
        return authorities;
    }

    public UserInfoDto toUserInfoDto(){
        return UserInfoDto.builder()
                .age(age)
                .gender(gender)
                .userName(userName)
                .build();
    }


    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    public String getUserName() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}