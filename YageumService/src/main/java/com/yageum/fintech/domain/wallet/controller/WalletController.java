package com.yageum.fintech.domain.wallet.controller;

import com.yageum.fintech.domain.tenant.dto.request.TenantProfileDto;
import com.yageum.fintech.domain.tenant.dto.response.GetTenantProfileDto;
import com.yageum.fintech.domain.wallet.infrastructure.Wallet;
import com.yageum.fintech.domain.wallet.service.WalletService;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "지갑 API", description = "메타마스크 주소 등록, 조회, 수정과 관련된 API")
@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WalletController {

    private final WalletService walletService;
    private final ResponseService responseService;

    //지갑 생성
    @Operation(summary = "사용자 지갑 계정 주소 등록", description = "사용자의 메타마스크 지갑 주소를 등록하는 API")
    @PostMapping("/{username}")
    public CommonResult registerWallet(@PathVariable String username, @RequestBody String accountAddress){
        return walletService.createWallet(username, accountAddress);
    }

    //지갑 조회
    @Operation(summary = "사용자 지갑 조회", description = "로그인한 사용자의 지갑 주소를 조회하는 API")
    @GetMapping("/{username}")
    public CommonResult getWallet(@PathVariable String username){
        //userId로 지갑 정보 조회
        Wallet wallet = walletService.getWalletByUsername(username);
        return responseService.getSingleResult(wallet);
    }

    //지갑 주소 수정
    @Operation(summary = "사용자 지갑 주소 수정", description = "사용자의 지갑 주소를 수정하는 API")
    @PatchMapping("/{username}")
    public CommonResult updateWallet(@PathVariable String username, @RequestBody String accountAddress){
        walletService.updateWallet(username, accountAddress);
        return responseService.getSuccessfulResult();
    }
}
