package com.yageum.fintech.domain.contract.controller;

import com.yageum.fintech.domain.contract.dto.ContractDetailsDto;
import com.yageum.fintech.domain.contract.infrastructure.ContractDetails;
import com.yageum.fintech.domain.contract.service.ContractDetailsService;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Result.CommonResult;
import com.yageum.fintech.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "계약서 API", description = "계약서 생성, 조회 API")
@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ContractDetailsController {

    private final ContractDetailsService contractDetailsService;
    private final ResponseService responseService;

    @Operation(summary = "계약 내용 저장", description = "해시값으로 계약 정보를 등록하는 API")
    @PostMapping()
    public CommonResult createContract(@RequestBody ContractDetailsDto contractDetailsDto) {
        contractDetailsService.saveContractDetails(contractDetailsDto);
        return responseService.getSuccessfulResult();
    }

    @Operation(summary = "계약 내용 조회", description = "해당 해쉬값을 가진 계약 내용을 조회하는 API")
    @GetMapping("/{hash}")
    public CommonResult getContract(@PathVariable String hash) {
        ContractDetails contractDetails = contractDetailsService.getContractDetails(hash);
        if (contractDetails != null) {
            return responseService.getSingleResult(contractDetails);
        } else {
            return responseService.getFailResult(ExceptionList.CONTRACT_DETAILS_NOT_FOUND.getCode(), ExceptionList.CONTRACT_DETAILS_NOT_FOUND.getMessage());
        }
    }
}
