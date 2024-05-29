package com.yageum.fintech.domain.auth.service;

import com.yageum.fintech.domain.auth.dto.request.LoginRequest;
import com.yageum.fintech.domain.auth.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.lessor.service.MailService;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import com.yageum.fintech.domain.tenant.infrastructure.TenantRepository;
import com.yageum.fintech.domain.auth.jwt.JwtTokenProvider;
import com.yageum.fintech.global.model.Exception.BusinessLogicException;
import com.yageum.fintech.global.model.Exception.EmailVerificationResult;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final RedisServiceImpl redisServiceImpl;
    private final TenantRepository tenantRepository;

    private final MailService mailService;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Transactional(readOnly = true)
    @Override
    public JWTAuthResponse tenantLogin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long tenantId = myUserDetailsService.findUserIdByUsername(loginRequest.getUsername());
        String name = myUserDetailsService.findNameByUsername(loginRequest.getUsername());
        JWTAuthResponse token = jwtTokenProvider.generateToken(loginRequest.getUsername(), authentication, tenantId, name);
        return token;
    }

    @Transactional(readOnly = true)
    @Override
    public JWTAuthResponse lessorLogin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long lessorId = myUserDetailsService.findUserIdByUsername(loginRequest.getUsername());
        String name = myUserDetailsService.findNameByUsername(loginRequest.getUsername());
        JWTAuthResponse token = jwtTokenProvider.generateToken(loginRequest.getUsername(), authentication, lessorId, name);
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

    @Transactional(readOnly = true)
    @Override
    public Optional<Tenant> findOne(String username) {
        return tenantRepository.findByUsername(username);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        //access token 검증
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedAccessException(ExceptionList.SIGNATURE_JWT);
        }
        //access token에서 인증정보 조회
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String username = authentication.getName(); //사용자 아이디

        //아이디로 조회되는 refresh token 이 있으면 삭제
        if(redisServiceImpl.getValues(username)!=null){
            redisServiceImpl.deleteValues(username);
        }

        //Access Token 남은 유효시간 가지고 와서 blacklist에 추가
        Long expiration = jwtTokenProvider.getAccessTokenExpiration(accessToken);
        redisServiceImpl.setBlackList(accessToken, "access_token", expiration);

        // Refresh Token 남은 유효시간 가지고 와서 blacklist에 추가
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            Long refreshTokenExpiration = jwtTokenProvider.getRefreshTokenExpiration(refreshToken);
            redisServiceImpl.setBlackList(refreshToken, "refresh_token", refreshTokenExpiration);
        }
    }

    private void verifiedRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new BusinessLogicException(ExceptionList.HEADER_REFRESH_TOKEN_NOT_EXISTS);
        }
    }

    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "YageumYageum 인증 코드";
        String authCode = this.createCode();

        // 인증 이메일 내용을 작성
        String emailContent = "안녕하세요,\n\n";
        emailContent += "귀하의 이메일 주소를 통해 YageumYageum 계정에 대한 인증이 요청되었습니다.";
        emailContent += "야금야금(YageumYageum)에서 발송한 이메일 인증 번호는 다음과 같습니다\n\n";
        emailContent += "인증 번호: " + authCode + "\n\n";
        emailContent += "이 인증 번호를 야금야금(YageumYageum) 웹 사이트 또는 애플리케이션에서 입력하여 이메일을 인증해주세요.\n\n";
        emailContent += "감사합니다.\n야금야금(YageumYageum) 서비스 운영팀";

        mailService.sendEmail(toEmail, title, emailContent);

        // 이메일 인증 요청 시 인증 번호 Redis에 저장( key = "AuthCode " + Email / value = AuthCode )
        redisServiceImpl.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    //중복 이메일 체크
    private void checkDuplicatedEmail(String email) {
        Optional<Tenant> userEntity = tenantRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            throw new BusinessLogicException(ExceptionList.MEMBER_EXISTS);
        }
    }

    //랜덤 인증번호 생성
    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessLogicException(ExceptionList.NO_SUCH_ALGORITHM);
        }
    }

    //인증번호 확인
    public EmailVerificationResult verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisServiceImpl.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisServiceImpl.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        return EmailVerificationResult.of(authResult);
    }

}
