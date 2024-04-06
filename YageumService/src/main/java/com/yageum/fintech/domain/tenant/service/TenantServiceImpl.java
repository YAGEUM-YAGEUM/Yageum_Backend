package com.yageum.fintech.domain.tenant.service;

import com.yageum.fintech.domain.tenant.dto.request.LoginRequest;
import com.yageum.fintech.domain.tenant.dto.request.TenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.request.TenantRequestDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantResponseDto;
import com.yageum.fintech.domain.tenant.infrastructure.TenantProfileRepository;
import com.yageum.fintech.global.model.Exception.*;
import com.yageum.fintech.domain.tenant.dto.response.JWTAuthResponse;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import com.yageum.fintech.domain.tenant.infrastructure.TenantRepository;
import com.yageum.fintech.global.config.jwt.JwtTokenProvider;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final MailService mailService;
    private final ResponseService responseService;
    private final TenantRepository tenantRepository;
    private final TenantProfileRepository tenantProfileRepository;

    private final BCryptPasswordEncoder pwdEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MyUserDetailsService myUserDetailsService;
    private final RedisServiceImpl redisServiceImpl;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Override
    public Optional<Tenant> findOne(String email) {
        return tenantRepository.findByEmail(email);
    }

    @Override
    public JWTAuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPwd()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long userId = myUserDetailsService.findUserIdByEmail(loginRequest.getEmail());
        JWTAuthResponse token = jwtTokenProvider.generateToken(loginRequest.getEmail(), authentication, userId);
        return token;
    }

    @Override
    public CommonResult register(TenantRequestDto tenantRequestDto) {

        // 중복 아이디 체크
        if(tenantRepository.existsById(tenantRequestDto.getUsername())){
            return responseService.getFailResult(ExceptionList.ALREADY_EXISTS.getCode(), ExceptionList.ALREADY_EXISTS.getMessage());
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Tenant tenant = mapper.map(tenantRequestDto, Tenant.class);
        tenant.setEncryptedPwd(pwdEncoder.encode(tenantRequestDto.getPassword()));
        tenantRepository.save(tenant);

        return responseService.getSuccessfulResultWithMessage("회원가입이 성공적으로 완료되었습니다!");
    }

    @Override
    public void createTenantProfile(Long tenantId, TenantProfileDto tenantProfileDto) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_TENANT));

        tenantProfileRepository.save(tenantProfileDto.toEntity(tenant));
    }

    @Transactional(readOnly = true)
    @Override
    public String getUsername(Long userId) {
        GetTenantResponseDto getTenantResponseDto = null;
        try{
            getTenantResponseDto = tenantRepository.findUserResponseByUserId(userId);
        }catch (RetryableException e){
            e.printStackTrace();
            return "(응답 시간 초과)";
        }catch (Exception e){
            e.printStackTrace();
            return "(알 수 없음)";
        }
        return getTenantResponseDto.getName();
    }

    @Transactional(readOnly = true)
    @Override
    public GetTenantResponseDto getUserResponseByUserId(Long userId) {
        GetTenantResponseDto userResponse = tenantRepository.findUserResponseByUserId(userId);
        if (userResponse == null) {
            throw new BusinessLogicException(ExceptionList.MEMBER_NOT_FOUND);
        }
        return userResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public GetTenantResponseDto getUserResponseByEmail(String email) {
        GetTenantResponseDto userResponse = tenantRepository.findUserResponseByEmail(email);
        if (userResponse == null) {
            throw new BusinessLogicException(ExceptionList.MEMBER_NOT_FOUND);
        }
        return userResponse;
    }

    @Override
    public JWTAuthResponse reissueAccessToken(String refreshToken) {
        this.verifiedRefreshToken(refreshToken);
        String email = jwtTokenProvider.getEmail(refreshToken);
        String redisRefreshToken = redisServiceImpl.getValues(email);

        if (redisServiceImpl.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)) {
            Optional<Tenant> findUser = this.findOne(email);
            Tenant tenant = Tenant.of(findUser);
            JWTAuthResponse tokenDto = jwtTokenProvider.generateToken(email, jwtTokenProvider.getAuthentication(refreshToken), tenant.getId());
            String newAccessToken = tokenDto.getAccessToken();
            long refreshTokenExpirationMillis = jwtTokenProvider.getRefreshTokenExpirationMillis();
            return tokenDto;
        } else throw new BusinessLogicException(ExceptionList.TOKEN_IS_NOT_SAME);
    }

    private void verifiedRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new BusinessLogicException(ExceptionList.HEADER_REFRESH_TOKEN_NOT_EXISTS);
        }
    }

    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "야금야금(YageumYageum) 회원가입 이메일 인증";
        String authCode = this.createCode();

        // 인증 이메일 내용을 작성
        String emailContent = "안녕하세요,\n\n";
        emailContent += "야금야금(YageumYageum)에서 발송한 이메일 인증 번호는 다음과 같습니다:\n\n";
        emailContent += "인증 번호: " + authCode + "\n\n";
        emailContent += "이 인증 번호를 야금야금(YageumYageum) 웹 사이트 또는 애플리케이션에서 입력하여 이메일을 인증해주세요.\n\n";
        emailContent += "감사합니다,\n야금야금(YageumYageum) 서비스 운영팀";

        mailService.sendEmail(toEmail, title, emailContent);

        // 이메일 인증 요청 시 인증 번호 Redis에 저장( key = "AuthCode " + Email / value = AuthCode )
        redisServiceImpl.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    //중복 이메일 체크
    private void checkDuplicatedEmail(String email) {
        Optional<Tenant> userEntity = tenantRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            log.debug("UserServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
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
            log.debug("MemberService.createCode() exception occur");
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
