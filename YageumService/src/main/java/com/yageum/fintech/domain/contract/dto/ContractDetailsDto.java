package com.yageum.fintech.domain.contract.dto;

import com.yageum.fintech.domain.contract.infrastructure.ContractDetails;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ContractDetailsDto {

    @NotNull
    private String hash;

    @NotNull
    private int deposit;

    @NotNull
    private int rentAmount;

    @NotNull
    private String propertyAddress;

    @NotNull
    private String specialTerms;

    @NotNull
    private String lessorSignaturePad;

    private String tenantSignaturePad;

    public ContractDetails toEntity() {
        return ContractDetails.builder()
                .hash(hash)
                .deposit(deposit)
                .rentAmount(rentAmount)
                .propertyAddress(propertyAddress)
                .specialTerms(specialTerms)
                .lessorSignaturePad(lessorSignaturePad)
                .tenantSignaturePad(tenantSignaturePad)
                .build();
    }
}
