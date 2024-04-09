package com.yageum.fintech.domain.auth.service;

import com.yageum.fintech.domain.auth.dto.request.LoginRequest;
import com.yageum.fintech.domain.auth.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import com.yageum.fintech.domain.tenant.infrastructure.TenantRepository;
import com.yageum.fintech.domain.tenant.service.RedisServiceImpl;
import com.yageum.fintech.domain.auth.jwt.JwtTokenProvider;
import com.yageum.fintech.global.model.Exception.BusinessLogicException;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final RedisServiceImpl redisServiceImpl;
    private final TenantRepository tenantRepository;

    @Transactional(readOnly = true)
    @Override
    public JWTAuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long tenantId = myUserDetailsService.findUserIdByUsername(loginRequest.getUsername());
        String name = myUserDetailsService.findNameByUsername(loginRequest.getUsername());
        JWTAuthResponse token = jwtTokenProvider.generateToken(loginRequest.getUsername(), authentication, tenantId, name);
        return token;
    }

    @Override
    public JWTAuthResponse reissueAccessToken(String refreshToken) {
        this.verifiedRefreshToken(refreshToken);
        String username = jwtTokenProvider.getUsername(refreshToken);
        String redisRefreshToken = redisServiceImpl.getValues(username);

        if (redisServiceImpl.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)) {
            Optional<Tenant> findUser = this.findOne(username);
            Tenant tenant = Tenant.of(findUser);
            JWTAuthResponse tokenDto = jwtTokenProvider.generateToken(username, jwtTokenProvider.getAuthentication(refreshToken), tenant.getTenantId(), tenant.getName());
            return tokenDto;
        } else throw new BusinessLogicException(ExceptionList.TOKEN_IS_NOT_SAME);
    }

    private void verifiedRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new BusinessLogicException(ExceptionList.HEADER_REFRESH_TOKEN_NOT_EXISTS);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Tenant> findOne(String username) {
        return tenantRepository.findByUsername(username);
    }

}
