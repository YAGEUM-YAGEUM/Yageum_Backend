package com.yageum.fintech.global.config.interceptor;

import com.yageum.fintech.domain.auth.jwt.JwtContextHolder;
import com.yageum.fintech.domain.auth.jwt.JwtTokenProvider;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NullJwtTokenException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (CorsUtils.isPreFlightRequest(request)) return true;

        //토큰 추출
        String jwtToken = jwtTokenProvider.resolveToken(request);

        //토큰이 있는지
        if (!StringUtils.hasText(jwtToken)) {
            throw new NullJwtTokenException(ExceptionList.NULL_JWT_TOKEN);
        }

        try {
            Claims claims = jwtTokenProvider.parseClaims(jwtToken);

            //토큰 내 정보 추출
            Long uid = claims.get("uid", Long.class); // 사용자 UID
            String name = claims.get("name", String.class); // 사용자 이름
            String username = claims.getSubject(); // 사용자 아이디

            if (uid == null || !StringUtils.hasText(name) || !StringUtils.hasText(username)) {
                throw new MalformedJwtException("JWT 토큰의 클레임이 잘못되었습니다.");
            }

            //JwtContextHolder 에 저장 (Thread)
            JwtContextHolder.setUid(uid); // 사용자 UID
            JwtContextHolder.setName(name); // 사용자 이름
            JwtContextHolder.setUsername(username); // 사용자 아이디

        } catch (JwtException e) {
            throw new IllegalArgumentException("JWT 토큰 검증 실패", e);
        }

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
