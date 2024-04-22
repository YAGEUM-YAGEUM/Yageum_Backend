package com.yageum.fintech.domain.wallet.service;

import com.yageum.fintech.domain.wallet.infrastructure.Wallet;
import com.yageum.fintech.global.model.Result.CommonResult;

public interface WalletService {
    CommonResult createWallet(String username, String accountAddress);

    Wallet getWalletByUsername(String username);

    void updateWallet(String username, String accountAddress);
}
