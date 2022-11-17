package com.perfume.allpouse.repository;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.entity.enumDirectory.LoginType;
import com.perfume.allpouse.data.entity.enumDirectory.Permission;
import com.perfume.allpouse.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 저장 테스트")
    public void save() throws Exception {
        //given
        User user = User.builder()
                .socialId("dongdong")
                .userName("dong")
                .age(27)
                .gender("남성")
                .loginType(LoginType.KAKAOTALK)
                .permission(Permission.GENERAL)
                .build();

        User savedUser = userRepository.save(user);
        Long savedId = savedUser.getId();

        assertThat(savedId).isEqualTo(1);
    }


    @Test
    @DisplayName("유저 한명 찾기 테스트")
    public void findOne() throws Exception{
        //given
        User user = User.builder()
                .socialId("dongdong")
                .userName("dong")
                .age(27)
                .gender("남성")
                .loginType(LoginType.KAKAOTALK)
                .permission(Permission.GENERAL)
                .build();

        //when
        User savedUser = userRepository.save(user);
        Long savedId = savedUser.getId();

        User findUser = userRepository.findById(savedId).get();

        assertThat(findUser.getAge()).isEqualTo(user.getAge());
    }

    @Test
    @DisplayName("cre_dt 테스트")
    public void timestampTest() throws Exception{
        //given
        User user = User.builder()
                .socialId("dongdong")
                .userName("dong")
                .age(27)
                .gender("남성")
                .loginType(LoginType.KAKAOTALK)
                .permission(Permission.GENERAL)
                .build();

        //when
        User savedUser = userRepository.save(user);

        //then
        assertThat(savedUser.getCreateDateTime()).isNotNull();
        assertThat(savedUser.getLastModifiedDateTime()).isNotNull();
    }

    @Test
    @DisplayName("수정시간 테스트")
    public void modifiedTimeTest() throws Exception{
        //given
        User user = User.builder()
                .socialId("dongdong")
                .userName("dong")
                .age(27)
                .gender("남성")
                .loginType(LoginType.KAKAOTALK)
                .permission(Permission.GENERAL)
                .build();


        //when
        User savedUser = userRepository.save(user);
        LocalDateTime firstSaveTime = savedUser.getCreateDateTime();
        System.out.println("firstSaveTime : "+firstSaveTime);

        savedUser.setAge(29);
        userRepository.save(savedUser);
        userRepository.flush();

        User findUser = userRepository.findById(savedUser.getId()).get();


        //then
        assertThat(firstSaveTime).isNotEqualTo(findUser.getLastModifiedDateTime());
    }
}