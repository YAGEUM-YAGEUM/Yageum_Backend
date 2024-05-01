// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./EnumDeclaration.sol";
import "./Agreement.sol";
import "@openzeppelin/contracts/utils/cryptography/SignatureChecker.sol";

contract RealEstateContract {
    // 공개 및 개인 키
    address public lessorPublicKey;
    address public lessorPrivateKey;
    address public tenantPublicKey;
    address public tenantPrivateKey;

    // 암호화된 계약 내용과 전자 서명
    bytes public encryptedContractContent;
    bytes public encryptedSignature;

    // 서명에 사용될 메시지의 해시값
    bytes32 public messageHash;

    // 반환되는 값
    event ContractInitiated(address indexed _lessor, uint _rentAmount, string _propertyAddress);
    event ContractAccepted(address indexed _tenant, uint _rentAmount);
    event ContractSigned(address indexed _signer, bool _isTenant);
    event ContractCompleted(address indexed _lessor, address indexed _tenant, uint _rentAmount);
    event ContractCanceled(address indexed _canceller, string _reason);

    constructor() {
        lessorPublicKey = msg.sender;
    }

    // 계약 상태 확인 modifier
    modifier onlyValidStatus(ContractStatus _status) {
        require(contractStatus == _status, "잘못된 상태입니다");
        _;
    }

    // 임대인이 매물 정보를 입력하고 계약서를 작성하여 임차인에게 보내는 함수
    function sendContract(address _tenant, uint _rentAmount, uint _deposit, uint _paymentSchedule, string memory _propertyAddress, string memory _specialTerms) public {
        require(msg.sender == lessorPublicKey, "임대인만 계약을 보낼 수 있습니다");

        tenantPublicKey = _tenant;
        Agreement agreement = new Agreement(_deposit, _rentAmount, _paymentSchedule, _propertyAddress, _specialTerms);
        messageHash = agreement.agreementHash();
        emit ContractInitiated(lessorPublicKey, agreement.agreementDetails.rentAmount, agreement.agreementDetails.propertyAddress);
    }

    // 임차인이 계약을 수락하는 함수
    function acceptLeaseContract() public {
        require(msg.sender == tenantPublicKey, "임차인만 계약을 수락할 수 있습니다");
        require(tenantPublicKey != address(0), "계약이 아직 수락되지 않았습니다");

        emit ContractAccepted(tenantPublicKey, agreement.agreementDetails.rentAmount);
    }

    // 공개 키로 데이터를 암호화하는 함수
    function encryptWithPublicKey(bytes memory _hashData, address _publicKey) private pure returns (bytes memory) {
        //수정 필요
        bytes memory encryptedData = abi.encodePacked(_hashData);
        return encryptedData;
    }

    // 주소를 문자열로 변환하는 함수
    function addressToString(address _addr) private pure returns(string memory) {
        bytes32 value = bytes32(uint256(_addr));
        bytes memory alphabet = "0123456789abcdef";

        bytes memory str = new bytes(42);
        str[0] = '0';
        str[1] = 'x';
        for (uint256 i = 0; i < 20; i++) {
            str[2+i*2] = alphabet[uint8(value[i + 12] >> 4)];
            str[3+i*2] = alphabet[uint8(value[i + 12] & 0x0f)];
        }
        return string(str);
    }

    // uint를 문자열로 변환하는 함수
    function uintToString(uint _i) private pure returns (string memory _uintAsString) {
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
        uint k = len - 1;
        while (_i != 0) {
            bstr[k--] = byte(uint8(48 + _i % 10));
            _i /= 10;
        }
        return string(bstr);
    }

    // 임차인 또는 임대인이 계약을 서명하는 함수
    function signContract(bytes memory _signature) public {
        require(msg.sender == tenantPublicKey || msg.sender == lessorPublicKey, "임차인 또는 임대인만 계약에 서명할 수 있습니다");
        require(messageHash != 0, "계약이 아직 초기화되지 않았습니다");

        bytes32 hash = keccak256(abi.encodePacked(messageHash, msg.sender == tenantPublicKey));
        require(SignatureChecker.isValidSignatureNow(msg.sender, hash, _signature), "잘못된 서명입니다");

        emit ContractSigned(msg.sender, msg.sender == tenantPublicKey);

        if (msg.sender == tenantPublicKey) {
            tenantPublicKey = msg.sender;
        } else {
            lessorPublicKey = msg.sender;
        }

        if (tenantPublicKey != address(0) && lessorPublicKey != address(0)) {
            completeTransaction();
        }
    }


    // 계약을 완료하는 함수
    function completeTransaction() private {
        require(tenantPublicKey != address(0) && lessorPublicKey != address(0), "임차인과 임대인 모두 계약에 서명해야 합니다");

        emit ContractCompleted(lessorPublicKey, tenantPublicKey, agreement.agreementDetails.rentAmount);
    }

}
