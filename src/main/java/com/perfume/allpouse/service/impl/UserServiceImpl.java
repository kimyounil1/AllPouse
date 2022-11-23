package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loadUserById(long id) {
        LOGGER.info("[loadUserById] 함수 실행. id : {}",id);
        return userRepository.getReferenceById(id);
    }

}
