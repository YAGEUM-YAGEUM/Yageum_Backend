package com.yageum.fintech.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl2 {

//    private final UserFeignClient userFeignClient;
//
//    public GetUserResponseDto getUser(Long userId){
//        try {return userFeignClient.getUser(userId).getBody();}
//        catch (Exception e){return null;}
//    }
//
//    public String getUsername(Long userId){
//        GetUserResponseDto getUserResponseDto = null;
//        try{
//            getUserResponseDto = userFeignClient.getUser(userId).getBody();
//        }catch (FeignException.InternalServerError e){
//            e.printStackTrace();
//            return "(내부 서버 오류)";
//        }catch (RetryableException e){
//            e.printStackTrace();
//            return "(응답 시간 초과)";
//        }catch (Exception e){
//            e.printStackTrace();
//            return "(알 수 없음)";
//        }
//        return getUserResponseDto.getName();
//    }
}
