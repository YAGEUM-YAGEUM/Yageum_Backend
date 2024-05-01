// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract Agreement {
    struct ContractDetails {
        uint deposit; // 보증금
        uint rentAmount; // 월세
        uint paymentSchedule; // 지불 일정
        string propertyAddress; // 매물 주소
        string specialTerms; // 특약사항
    }

    string public agreementDetails; // 계약 내용
    bytes32 public agreementHash; // 암호화된 계약 내용의 해시값

    constructor(uint _deposit, uint _rentAmount, uint _paymentSchedule, string memory _propertyAddress, string memory _specialTerms) {
        ContractDetails memory details = ContractDetails(_deposit, _rentAmount, _paymentSchedule, _propertyAddress, _specialTerms);
        agreementDetails = generateContractDetailsString(details);
        agreementHash = generateHash(agreementDetails);
    }

    // 계약 내용 구조체를 문자열로 변환하는 함수
    function generateContractDetailsString(ContractDetails memory details) private pure returns (string memory) {
        return string(abi.encodePacked(
            "Deposit: ", uint2str(details.deposit), "\n", // 보증금
            "Rent Amount: ", uint2str(details.rentAmount), "\n", // 월세
            "Payment Schedule: ", uint2str(details.paymentSchedule), "\n", // 지불 일정
            "Property Address: ", details.propertyAddress, "\n", // 매물 주소
            "Special Terms: ", details.specialTerms, "\n" // 특약사항
        ));
    }

    // uint 값을 문자열로 변환하는 함수
    function uint2str(uint _i) private pure returns (string memory _uintAsString) {
        if (_i == 0) {
            return "0";
        }
        uint j = _i;
        uint len;
        while (j != 0) {
            len++;
            j /= 10;
        }
        bytes memory bstr = new bytes(len);
        uint k = len;
        while (_i != 0) {
            k = k-1;
            uint8 temp = (48 + uint8(_i - _i / 10 * 10));
            bytes1 b1 = bytes1(temp);
            bstr[k] = b1;
            _i /= 10;
        }
        return string(bstr);
    }

    // 계약 내용을 해시하여 반환하는 함수
    function generateHash(string memory _data) private pure returns (bytes32) {
        return keccak256(abi.encodePacked(_data));
    }

    // 계약 내용을 업데이트하는 함수
    function updateAgreementDetails(uint _deposit, uint _rentAmount, uint _paymentSchedule, string memory _propertyAddress, string memory _specialTerms) public {
        ContractDetails memory updatedDetails = ContractDetails(_deposit, _rentAmount, _paymentSchedule, _propertyAddress, _specialTerms);
        agreementDetails = generateContractDetailsString(updatedDetails);
        agreementHash = generateHash(agreementDetails);
    }

}
