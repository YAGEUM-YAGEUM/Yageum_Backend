// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./EnumDeclaration.sol";
import "./Agreement.sol";
import "@openzeppelin/contracts/utils/cryptography/SignatureChecker.sol";
import "@openzeppelin/contracts/utils/structs/EnumerableSet.sol";

contract RealEstateContract {
    // 공개 키
    address public lessorPublicKey;
    address public tenantPublicKey;

    // 계약서 내용 해시값
    bytes32 public messageHash;

    // 사용자의 서명을 저장하는 매핑
    mapping(address => bytes) public signatures;

    // 계약 상태
    ContractStatus public contractStatus;

    //해시값 - 계약서 내용 원본 값 매핑
    mapping(bytes32 => string) private dataByHash;

    // 반환되는 값
    event ContractInitiated(address indexed _lessor, uint _rentAmount, string _propertyAddress);
    event ContractAccepted(address indexed _tenant);
    event ContractSigned(address indexed _signer, bool _isTenant);
    event ContractCompleted(address indexed _lessor, address indexed _tenant);
    event ContractCanceled(address indexed _canceller, string _reason);

    constructor() {
        lessorPublicKey = msg.sender;
    }

    // 임차인 또는 임대인 modifier
    modifier onlyTenantOrLessor() {
        require(msg.sender == tenantPublicKey || msg.sender == lessorPublicKey, "임차인 또는 임대인만 계약에 서명할 수 있습니다");
        _;
    }

    // Getter 함수를 통해 계약 상태를 클라이언트에 제공
    function getContractStatus() public view returns (ContractStatus) {
        require(msg.sender == tenantPublicKey || msg.sender == lessorPublicKey, "임대인 또는 임차인만 조회할 수 있습니다");
        return contractStatus;
    }

    // Getter 함수를 통해 계약서 내용 해시값을 클라이언트에 제공
    function getMessageHash() public view returns (bytes32) {
        require(msg.sender == tenantPublicKey || msg.sender == lessorPublicKey, "임대인 또는 임차인만 조회할 수 있습니다");
        return messageHash;
    }

    // 해시 값에 해당하는 매물 정보를 조회하는 함수
    function getPropertyDataByHash(bytes32 _hash) public view returns (string memory) {
        return dataByHash[_hash];
    }

    // 임대인이 매물 정보를 입력하고 계약서를 작성하여 임차인에게 보내는 함수
    function sendContract(address _tenant, uint _rentAmount, uint _deposit, uint _paymentSchedule, string memory _propertyAddress, string memory _specialTerms) public {
        require(msg.sender == lessorPublicKey, "임대인만 계약을 보낼 수 있습니다");

        Agreement agreement = new Agreement(_deposit, _rentAmount, _paymentSchedule, _propertyAddress, _specialTerms);

        string memory propertyData = agreement.agreementDetails();
        messageHash = agreement.agreementHash();

        // 해시-데이터 매핑에 매물 정보 추가
        dataByHash[messageHash] = propertyData;

        tenantPublicKey = _tenant;
        contractStatus = ContractStatus.Initiated;

        emit ContractInitiated(lessorPublicKey, _rentAmount, _propertyAddress);
    }

    // 임차인이 계약을 수락하는 함수
    function acceptLeaseContract() public {
        require(msg.sender == tenantPublicKey, "임차인만 계약을 수락할 수 있습니다");
        require(tenantPublicKey != address(0), "계약이 아직 수락되지 않았습니다");

        contractStatus = ContractStatus.Accepted;
        emit ContractAccepted(tenantPublicKey);
    }

    // 임차인 또는 임대인이 계약을 서명하는 함수
    function signContract(bytes memory _signature) public onlyTenantOrLessor {
        require(contractStatus == ContractStatus.Accepted, "계약이 아직 수락되지 않았습니다");
        require(messageHash != 0, "계약이 아직 초기화되지 않았습니다");

        // 계약서의 내용과 서명자에 따른 해시 값 계산
        bytes32 hash = keccak256(abi.encodePacked(messageHash, msg.sender == tenantPublicKey));
        require(SignatureChecker.isValidSignatureNow(msg.sender, hash, _signature), "잘못된 서명입니다");

        emit ContractSigned(msg.sender, msg.sender == tenantPublicKey);

        if (msg.sender == tenantPublicKey) {
            tenantPublicKey = msg.sender;
        } else {
            lessorPublicKey = msg.sender;
        }

        // 사용자의 서명을 저장
        signatures[msg.sender] = _signature;

        // 양쪽 사용자가 모두 서명했는지 확인하고, 모든 조건이 충족되면 계약을 완료
        if (signatures[tenantPublicKey].length > 0 && signatures[lessorPublicKey].length > 0) {
            contractStatus = ContractStatus.Signed;
            completeTransaction();
        }

    }

    // 계약을 완료하고 완료된 계약의 주소를 반환하는 함수
    function completeTransaction() public returns (address) {
        require(tenantPublicKey != address(0) && lessorPublicKey != address(0), "임차인과 임대인 모두 계약에 서명해야 합니다");
        require(contractStatus != ContractStatus.Completed, "이미 계약이 완료되었습니다.");

        contractStatus = ContractStatus.Completed;
        emit ContractCompleted(lessorPublicKey, tenantPublicKey);

        return address(this);

    }

    // 계약을 취소하는 함수
    function cancelContract(string memory _reason)  {
        require(msg.sender == tenantPublicKey || msg.sender == lessorPublicKey, "임차인 또는 임대인만 계약을 취소할 수 있습니다.");
        require(contractStatus != ContractStatus.Canceled, "이미 취소된 계약입니다");
        require(contractStatus != ContractStatus.Completed, "이미 완료된 계약은 취소할 수 없습니다");
        require(contractStatus != ContractStatus.Signed, "서명이 완료된 계약은 취소할 수 없습니다");

        contractStatus = ContractStatus.Canceled;
        emit ContractCanceled(msg.sender, _reason);
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

}
