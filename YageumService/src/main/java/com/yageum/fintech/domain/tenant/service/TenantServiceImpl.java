package com.yageum.fintech.domain.tenant.service;

import com.yageum.fintech.domain.lessor.infrastructure.LessorRepository;
import com.yageum.fintech.domain.tenant.dto.request.TenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.request.TenantRequestDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantResponseDto;
import com.yageum.fintech.domain.tenant.infrastructure.TenantProfile;
import com.yageum.fintech.domain.tenant.infrastructure.TenantProfileRepository;
import com.yageum.fintech.global.model.Exception.*;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import com.yageum.fintech.domain.tenant.infrastructure.TenantRepository;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final LessorRepository lessorRepository;
    private final TenantProfileRepository tenantProfileRepository;

    private final BCryptPasswordEncoder pwdEncoder;
    private final ResponseService responseService;

    @Override
    public CommonResult register(TenantRequestDto tenantRequestDto) {

        /* 임차인 중복 아이디 체크 */
        if(tenantRepository.existsByUsername(tenantRequestDto.getUsername())){
            return responseService.getFailResult(ExceptionList.ALREADY_EXISTS.getCode(), ExceptionList.ALREADY_EXISTS.getMessage());
        }

        /* 임대인 중복 아이디 체크 */
        if(lessorRepository.existsByUsername(tenantRequestDto.getUsername())){
            return responseService.getFailResult(ExceptionList.ALREADY_EXISTS.getCode(), ExceptionList.ALREADY_EXISTS.getMessage());
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Tenant tenant = mapper.map(tenantRequestDto, Tenant.class);
        tenant.setEncryptedPwd(pwdEncoder.encode(tenantRequestDto.getPassword()));
        tenantRepository.save(tenant);

        return responseService.getSuccessfulResultWithMessage("임차인 회원가입이 성공적으로 완료되었습니다!");
    }

    @Override
    public void createTenantProfile(Long tenantId, TenantProfileDto tenantProfileDto) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_TENANT));

        tenantProfileRepository.save(tenantProfileDto.toEntity(tenant));
    }

    @Override
    public GetTenantProfileDto getTenantProfile(Long tenantId) {
        TenantProfile tenantProfile = tenantProfileRepository.findByTenant_TenantId(tenantId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_TENANT_PROFILE));
        Tenant tenant = tenantProfile.getTenant();
        return GetTenantProfileDto.from(tenant, tenantProfile);
    }

    @Override
    public void updateTenantProfile(Long profileId, TenantProfileDto tenantProfileDto) {
        TenantProfile profile = tenantProfileRepository.findByProfileId(profileId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_TENANT));

        profile.update(tenantProfileDto);
    }


    @Transactional(readOnly = true)
    @Override
    public String getUsername(Long userId) {
        GetTenantResponseDto getTenantResponseDto = null;
        try{
            getTenantResponseDto = tenantRepository.findUserResponseByUserId(userId);
        }catch (TimeoutException e){
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

}
