package com.perfume.allpouse.service;


import com.perfume.allpouse.model.dto.SignDto;

public interface SignService {

    boolean signUp(String socialId, String userName, String role, int age, String gender, String loginType);

    SignDto signIn(String socialId, String loginType);

}
