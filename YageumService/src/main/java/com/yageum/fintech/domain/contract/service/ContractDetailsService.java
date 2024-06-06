package com.yageum.fintech.domain.contract.service;

import com.yageum.fintech.domain.contract.dto.ContractDetailsDto;
import com.yageum.fintech.domain.contract.infrastructure.ContractDetails;

public interface ContractDetailsService {

    void saveContractDetails(ContractDetailsDto contractDetailsDto);

    ContractDetails getContractDetails(String hash);

}
