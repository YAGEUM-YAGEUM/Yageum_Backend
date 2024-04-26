// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

contract SignatureVerifier {
    mapping(bytes32 => bool) public verifiedSignatures;

    // 서명된 데이터의 해시값을 계산하여 저장하는 함수
    function storeSignature(bytes memory _signedData) public {
        bytes32 dataHash = keccak256(_signedData);
        // 해시값 저장
        verifiedSignatures[dataHash] = true;
    }

    // 특정 서명된 데이터의 유효성을 검증하는 함수
    function verifySignature(bytes memory _signedData) public view returns (bool) {
        bytes32 dataHash = keccak256(_signedData);
        // 저장된 해시값이 존재하는지 확인하여 서명의 유효성 검증
        return verifiedSignatures[dataHash];
    }
}
