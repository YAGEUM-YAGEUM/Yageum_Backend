// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Agreement {
    struct ContractDetails {
        uint deposit; // 보증금
        uint rentAmount; // 월세
        uint paymentSchedule; // 지불 일정
        string propertyAddress; // 주소
        string specialTerms; // 특약사항
    }

    ContractDetails public agreementDetails;
    bytes32 public agreementHash; // 암호화된 계약 내용의 해시값

    constructor(uint _deposit, uint _rentAmount, uint _paymentSchedule, string memory _propertyAddress, string memory _specialTerms) {
        agreementDetails = ContractDetails(_deposit, _rentAmount, _paymentSchedule, _propertyAddress, _specialTerms);
        agreementHash = generateHash(agreementDetails);
    }

    // 계약 내용을 해시하여 반환하는 함수
    function generateHash(ContractDetails memory details) private pure returns (bytes32) {
        return keccak256(abi.encodePacked(details.deposit, details.rentAmount, details.paymentSchedule, details.propertyAddress, details.specialTerms));
    }

    // 계약 내용을 업데이트하는 함수
    function updateAgreementDetails(uint _deposit, uint _rentAmount, uint _paymentSchedule, string memory _propertyAddress, string memory _specialTerms) public {
        agreementDetails.deposit = _deposit;
        agreementDetails.rentAmount = _rentAmount;
        agreementDetails.paymentSchedule = _paymentSchedule;
        agreementDetails.propertyAddress = _propertyAddress;
        agreementDetails.specialTerms = _specialTerms;
        agreementHash = generateHash(agreementDetails);
    }
}
