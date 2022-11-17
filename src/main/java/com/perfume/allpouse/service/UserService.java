package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;


    // User저장(ID값 반환)
    @Transactional
    public Long save(User user){
        validateUser(user);
        User saveUser = userRepository.save(user);
        return saveUser.getId();
    }


    // 중복 회원 검증 로직
    private void validateUser(User user) {

    }

    /*
     * User 조회
     */
    // 1. 단건 조회
    public User findOne(Long id){
        Optional<User> findUser = userRepository.findById(id);

        if(findUser.isPresent()){
            return findUser.get();
        } else {
            throw new IllegalStateException();
        }
    }

    // 2. 전체 회원 조회
    public List<User> findUsers() {
        return userRepository.findAll();
    }
}
