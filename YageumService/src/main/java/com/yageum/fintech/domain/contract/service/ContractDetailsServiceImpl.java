package com.yageum.fintech.domain.contract.service;

import com.yageum.fintech.domain.contract.dto.ContractDetailsDto;
import com.yageum.fintech.domain.contract.infrastructure.ContractDetails;
import com.yageum.fintech.domain.contract.infrastructure.ContractDetailsRepository;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContractDetailsServiceImpl implements ContractDetailsService{

    private final ContractDetailsRepository contractDetailsRepository;

    @Transactional
    @Override
    public void saveContractDetails(ContractDetailsDto contractDetailsDto) {
        contractDetailsRepository.save(contractDetailsDto.toEntity());

    }

    @Override
    public ContractDetails getContractDetails(String hash) {
        ContractDetails contractDetails = contractDetailsRepository.findByHash(hash)
                .orElseThrow(()-> new NonExistentException(ExceptionList.CONTRACT_DETAILS_NOT_FOUND));

        return contractDetails;
    }
}
