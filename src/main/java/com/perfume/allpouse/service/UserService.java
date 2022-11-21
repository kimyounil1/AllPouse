package com.perfume.allpouse.service;


import com.perfume.allpouse.data.entity.User;

public interface UserService {

    User loadUserBySocialId(String userName);

}
