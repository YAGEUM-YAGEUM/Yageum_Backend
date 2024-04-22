package com.yageum.fintech.domain.wallet.service;

import com.yageum.fintech.domain.tenant.infrastructure.TenantProfile;
import com.yageum.fintech.domain.wallet.infrastructure.Wallet;
import com.yageum.fintech.domain.wallet.infrastructure.WalletRepository;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;
    private final ResponseService responseService;

    @Override
    public CommonResult createWallet(String username, String accountAddress) {

        /* 임차인 중복 아이디 체크 */
        if(walletRepository.existsByUsername(username)){
            return responseService.getFailResult(ExceptionList.ALREADY_EXISTS_WALLET.getCode(), ExceptionList.ALREADY_EXISTS_WALLET.getMessage());
        }

        Wallet wallet = new Wallet();

        wallet.setUsername(username);
        wallet.setAccountAddress(accountAddress);

        walletRepository.save(wallet);

        return responseService.getSuccessfulResultWithMessage("메타마스크 주소 등록이 성공적으로 완료되었습니다!");

    }

    @Override
    public Wallet getWalletByUsername(String username) {
        Wallet wallet = walletRepository.findByUsername(username);
        if(wallet == null)
            throw new NonExistentException(ExceptionList.NON_EXISTENT_WALLET);

        return walletRepository.findByUsername(username);
    }

    @Override
    public void updateWallet(String username, String accountAddress) {
        Wallet wallet = walletRepository.findByUsername(username);
        if(wallet == null)
            throw new NonExistentException(ExceptionList.NON_EXISTENT_WALLET);

        wallet.setAccountAddress(accountAddress);
        walletRepository.save(wallet);
    }
}
