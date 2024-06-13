// SPDX-License-Identifier: MIT
pragma solidity ^0.8.21;

import "@openzeppelin/contracts/utils/cryptography/SignatureChecker.sol";
import "@openzeppelin/contracts/utils/structs/EnumerableSet.sol";

contract RealEstateContract {

    struct ContractDetails {
        uint deposit; // 보증금
        uint rentAmount; // 월세
        string propertyAddress; // 매물 주소
        string specialTerms; // 특약사항
        string lessorSignaturePad; // 임대인의 사인패드 서명 값
        string tenantSignaturePad; // 임차인의 사인패드 서명 값
    }

    enum ContractStatus {
        Initiated,
        Accepted,
        Signed,
        Completed,
        Canceled
    }

    // 공개 키
    address public lessorPublicKey;
    address public tenantPublicKey;

    //원본 계약 정보
    ContractDetails public contractDetails;

    // 계약서 내용 해시값
    bytes32 public messageHash;

    // 사용자의 서명을 저장하는 매핑
    mapping(address => bytes) public signatures;

    // 계약 상태
    ContractStatus public contractStatus;

    // 반환되는 값
    event ContractInitiated(address indexed _contractAddress, address indexed _lessor, uint _rentAmount, string _propertyAddress);
    event TenantSignatureUpdated(address indexed _tenant, string _tenantSignaturePad);
    event ContractSigned(address indexed _signer, bool _isTenant);
    event ContractCompleted(address indexed _contractAddress, address indexed _lessor, address indexed _tenant);
    event ContractCanceled(address indexed _canceller, string _reason);

    constructor() {
        lessorPublicKey = msg.sender;
    }

    // 임차인 또는 임대인 modifier
    modifier onlyTenantOrLessor() {
        require(msg.sender == tenantPublicKey || msg.sender == lessorPublicKey, "Only the tenant or lessor can sign the contract");
        _;
    }

    // Getter 함수를 통해 계약 상태를 클라이언트에 제공
    function getContractStatus() public view returns (ContractStatus) {
        require(msg.sender == tenantPublicKey || msg.sender == lessorPublicKey, "Only the lessor or tenant can view");
        return contractStatus;
    }

    //매물 정보를 조회하는 함수
    function getContractDetails() public view returns (uint, uint, string memory, string memory) {
        return (
            contractDetails.deposit,
            contractDetails.rentAmount,
            contractDetails.propertyAddress,
            contractDetails.specialTerms
        );
    }

    // 임대인이 매물 정보를 입력하고 계약서를 작성하여 임차인에게 보내는 함수
    function sendContract(address _tenant, uint _rentAmount, uint _deposit, string memory _propertyAddress, string memory _specialTerms, string memory _lessorSignaturePad) public {
        require(msg.sender == lessorPublicKey, "Only the lessor can send a contract");

        contractDetails = ContractDetails({
            deposit: _deposit,
            rentAmount: _rentAmount,
            propertyAddress: _propertyAddress,
            specialTerms: _specialTerms,
            lessorSignaturePad: _lessorSignaturePad,
            tenantSignaturePad: ""
        });

        // 계약서 내용을 해시화하여 messageHash 설정
        messageHash = hashContractDetails(_deposit, _rentAmount, _propertyAddress, _specialTerms, _lessorSignaturePad, "");

        tenantPublicKey = _tenant;
        contractStatus = ContractStatus.Initiated;

        emit ContractInitiated(address(this), lessorPublicKey, _rentAmount, _propertyAddress);
    }

    // 임차인이 서명 패드로 한 사인값을 업데이트하는 함수
    function updateTenantSignaturePad(string memory _tenantSignaturePad) public onlyTenantOrLessor {
        require(msg.sender == tenantPublicKey, "Only the tenant can update the signature pad");

        // 계약서 내용을 확인한 후 임차인의 사인 패드 값을 업데이트
        contractDetails.tenantSignaturePad = _tenantSignaturePad;

        // 계약서 내용을 해시화하여 messageHash를 업데이트
        messageHash = hashContractDetails(
            contractDetails.deposit,
            contractDetails.rentAmount,
            contractDetails.propertyAddress,
            contractDetails.specialTerms,
            contractDetails.lessorSignaturePad,
            contractDetails.tenantSignaturePad);

        contractStatus = ContractStatus.Accepted;
        emit TenantSignatureUpdated(tenantPublicKey, _tenantSignaturePad);
    }

    // 임차인 또는 임대인이 계약을 서명하는 함수
    function signContract(bytes memory _signature) public onlyTenantOrLessor {
        require(messageHash != 0, "The contract has not been initialized yet");

        // 계약서의 내용과 서명자에 따른 해시 값 계산(서명값 생성)
        bytes32 hash = keccak256(abi.encodePacked(messageHash, msg.sender == tenantPublicKey));
        require(SignatureChecker.isValidSignatureNow(msg.sender, hash, _signature), "Invalid signature");

        //임차인인지 아닌지 정보 포함
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
    function completeTransaction() public {
        require(tenantPublicKey != address(0) && lessorPublicKey != address(0), "Both the tenant and lessor must sign the contract");
        require(contractStatus != ContractStatus.Completed, "The contract has already been completed");

        contractStatus = ContractStatus.Completed;
        emit ContractCompleted(address(this),lessorPublicKey, tenantPublicKey);
    }

    // 계약을 취소하는 함수
    function cancelContract(string memory _reason)  public{
        require(msg.sender == tenantPublicKey || msg.sender == lessorPublicKey, "Only the tenant or lessor can cancel the contract");
        require(contractStatus != ContractStatus.Canceled, "The contract has already been canceled");
        require(contractStatus != ContractStatus.Completed, "A completed contract cannot be canceled");
        require(contractStatus != ContractStatus.Signed, "A signed contract cannot be canceled");

        contractStatus = ContractStatus.Canceled;
        emit ContractCanceled(msg.sender, _reason);
    }

    // 계약서 내용을 해시화하여 messageHash를 설정하는 함수
    function hashContractDetails(uint _deposit, uint _rentAmount, string memory _propertyAddress, string memory _specialTerms, string memory _lessorSignaturePad, string memory _tenantSignaturePad) internal pure returns (bytes32) {
        return keccak256(abi.encodePacked(_deposit, _rentAmount, _propertyAddress, _specialTerms, _lessorSignaturePad, _tenantSignaturePad));
    }

}
