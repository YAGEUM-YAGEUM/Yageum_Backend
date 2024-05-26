package com.yageum.fintech.global.config.interceptor;

import com.yageum.fintech.domain.auth.jwt.JwtContextHolder;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NullJwtTokenException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(CorsUtils.isPreFlightRequest(request)) return true;

        String jwtToken = request.getHeader("Authorization");

        //token의 값이 정상적인지 확인
        if(!StringUtils.hasText(jwtToken)) throw new NullJwtTokenException(ExceptionList.NULL_JWT_TOKEN);
        if(!jwtToken.startsWith("Bearer ")) throw new UnsupportedJwtException("");

        //토큰 추출
        String token = jwtToken.substring(7);

        //토큰 유효성 검증 : 유효하지 않으면 파싱되지 않음
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        Long uid = claims.get("uid", Long.class); //사용자 UID
        String name = claims.get("name", String.class); //사용자 이름
        String username = claims.getSubject(); //사용자 아이디
        if(uid==null||!StringUtils.hasText(name)||!StringUtils.hasText(username))
            throw new MalformedJwtException("");


        JwtContextHolder.setUid(claims.get("uid", Long.class)); //사용자 UID
        JwtContextHolder.setName(claims.get("name", String.class)); //사용자 이름
        JwtContextHolder.setUsername(claims.getSubject()); //사용자 아이디
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        JwtContextHolder.clear();
    }
}
