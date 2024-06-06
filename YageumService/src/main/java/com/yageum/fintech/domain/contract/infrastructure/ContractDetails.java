package com.yageum.fintech.domain.contract.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "contractDetails")
public class ContractDetails {

    @Id
    private String hash;

    private int deposit;
    private int rentAmount;
    private String propertyAddress;
    private String specialTerms;
    private String lessorSignaturePad;
    private String tenantSignaturePad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractDetails that = (ContractDetails) o;
        return Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }

    @Builder
    public ContractDetails(String hash, int deposit, int rentAmount, String propertyAddress, String specialTerms, String lessorSignaturePad, String tenantSignaturePad) {
        this.hash = hash;
        this.deposit = deposit;
        this.rentAmount = rentAmount;
        this.propertyAddress = propertyAddress;
        this.specialTerms = specialTerms;
        this.lessorSignaturePad = lessorSignaturePad;
        this.tenantSignaturePad = tenantSignaturePad;
    }
}
