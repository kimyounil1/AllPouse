package com.perfume.allpouse.service;


import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.model.dto.UserInfoDto;

public interface UserService {

    User loadUserById(long id);

    public UserInfoDto getUserInfoDtoById(Long id);

    User findOne(Long id);

}
