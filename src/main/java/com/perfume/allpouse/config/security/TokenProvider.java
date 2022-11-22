package com.perfume.allpouse.config.security;

import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider implements InitializingBean{

    private final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);

    private final UserServiceImpl userServiceImpl;

    public TokenProvider(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Value("${springboot.jwt.secret}")
    private String secretKey;
    private Key key;
    private final long tokenValidMillisecond = 1000L * 60 * 60 * 24 * 30;


    @Override
    public void afterPropertiesSet() {
        LOGGER.info("INIT : JWT SecretKey 초기화 시작");
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)); // 22년 이후 BASE64에서 HMAC 사용을 권장
        LOGGER.info("INIT : JWT SecretKey 초기화 완료");
    }

    public String getSocialId(String token) {
        LOGGER.info("[getUserName] 토큰에서 회원 ID 추출 ");
        String socialId = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        LOGGER.info("[getUserName] 토큰에서 회원 ID 추출 완료 socialID : {}" ,socialId);
        return socialId;
    }

    public Long getUserId(String token) {
        LOGGER.info("[getUserId] 토큰에서 회원 저장 ID 추출");
        Long userId = (Long) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("id");
        LOGGER.info("[getUserId] 토큰에서 회원 저장 ID 추출 완료 userId : {}", userId);
        return userId;
    }

    public String createToken(String socialId , String roles, long id) {

        Claims claims = Jwts.claims().setSubject(socialId);
        claims.put("roles", roles);
        claims.put("id", id);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public Authentication getAuthentication (String token) {

        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        User user = userServiceImpl.loadUserBySocialId(this.getSocialId(token));
        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 완료 user : {}", user.toString());

        return new UsernamePasswordAuthenticationToken(user, "",user.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        LOGGER.info("[resolveToken] 토큰 정보 헤더에서 읽어오기 ");
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String token) {
        LOGGER.info("[validateToken] 토큰 유효성 검증");
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            LOGGER.info("[validateToken] 토큰 유효성 예외 발생 ");
            return false;
        }


    }

}
