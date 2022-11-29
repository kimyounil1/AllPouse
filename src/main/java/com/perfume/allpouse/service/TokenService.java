package com.perfume.allpouse.service;

import com.amazonaws.Request;
import com.perfume.allpouse.model.dto.SignDto;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {

    SignDto createToken(HttpServletRequest request);

}
