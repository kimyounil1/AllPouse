package com.perfume.allpouse.service;


import com.perfume.allpouse.data.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    User loadUserByUserName(String userName);

}
