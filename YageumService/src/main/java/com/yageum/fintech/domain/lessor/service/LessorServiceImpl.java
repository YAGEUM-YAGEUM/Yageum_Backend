package com.yageum.fintech.domain.lessor.service;

import com.yageum.fintech.domain.lessor.dto.request.LessorProfileDto;
import com.yageum.fintech.domain.lessor.dto.request.LessorRequestDto;
import com.yageum.fintech.domain.lessor.dto.response.GetLessorProfileDto;
import com.yageum.fintech.domain.lessor.infrastructure.Lessor;
import com.yageum.fintech.domain.lessor.infrastructure.LessorProfile;
import com.yageum.fintech.domain.lessor.infrastructure.LessorProfileRepository;
import com.yageum.fintech.domain.lessor.infrastructure.LessorRepository;
import com.yageum.fintech.domain.tenant.infrastructure.TenantRepository;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
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
public class LessorServiceImpl implements LessorService{

    private final TenantRepository tenantRepository;
    private final LessorRepository lessorRepository;
    private final LessorProfileRepository lessorProfileRepository;
    private final ResponseService responseService;
    private final BCryptPasswordEncoder pwdEncoder;

    @Override
    public CommonResult register(LessorRequestDto lessorRequestDto) {
        /* 임차인 중복 아이디 체크 */
        if(tenantRepository.existsByUsername(lessorRequestDto.getUsername())){
            return responseService.getFailResult(ExceptionList.ALREADY_EXISTS.getCode(), ExceptionList.ALREADY_EXISTS.getMessage());
        }

        /* 임대인 중복 아이디 체크 */
        if(lessorRepository.existsByUsername(lessorRequestDto.getUsername())){
            return responseService.getFailResult(ExceptionList.ALREADY_EXISTS.getCode(), ExceptionList.ALREADY_EXISTS.getMessage());
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Lessor lessor = mapper.map(lessorRequestDto, Lessor.class);
        lessor.setEncryptedPwd(pwdEncoder.encode(lessorRequestDto.getPassword()));
        lessorRepository.save(lessor);

        return responseService.getSuccessfulResultWithMessage("임대인 회원가입이 성공적으로 완료되었습니다!");

    }

    @Override
    public void createLessorProfile(Long lessorId, LessorProfileDto lessorProfileDto) {
        Lessor lessor = lessorRepository.findByLessorId(lessorId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_LESSOR));
        /*
        토큰 인증 추가
         */
        lessorProfileRepository.save(lessorProfileDto.toEntity(lessor));

    }

    @Override
    public void updateLessorProfile(Long profileId, LessorProfileDto lessorProfileDto) {
        LessorProfile profile = lessorProfileRepository.findByProfileId(profileId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_LESSOR_PROFILE));
        /*
        토큰 인증 추가
         */
        profile.update(lessorProfileDto);
    }

    @Override
    public GetLessorProfileDto getLessorProfile(Long lessorId) {
        LessorProfile lessorProfile = lessorProfileRepository.findByLessor_LessorId(lessorId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_LESSOR_PROFILE));
        Lessor lessor = lessorProfile.getLessor();
        return GetLessorProfileDto.from(lessor, lessorProfile);
    }
}
