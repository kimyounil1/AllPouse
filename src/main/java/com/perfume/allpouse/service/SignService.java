package com.perfume.allpouse.service;


public interface SignService {

    boolean signUp(String socialId, String userName, String role, int age, String gender, String loginType);

    String signIn(String socialId, String loginType);

}
