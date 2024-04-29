// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "./EnumDeclaration.sol";
import "./Agreement.sol";

contract RealEstateContract {
    address public lessor; // 임대인
    address public tenant; // 임차인
    uint public rentAmount; // 임대료
    Agreement public agreement; // 계약 내용
    mapping(address => bool) public hasSigned; // 서명 여부 저장
    bool public contractCompleted; // 계약 완료 여부
    ContractStatus public contractStatus; // 계약 상태

    // 반환되는 값
    event ContractInitiated(address indexed _lessor, uint _rentAmount, string _propertyAddress);
    event ContractAccepted(address indexed _tenant, uint _rentAmount);
    event ContractSigned(address indexed _signer, bool _isTenant);
    event ContractCompleted(address indexed _lessor, address indexed _tenant, uint _rentAmount);
    event ContractCanceled(address indexed _canceller, string _reason);

    constructor() {
        lessor = msg.sender;
        contractCompleted = false;
        contractStatus = ContractStatus.Initiated;
    }

    // 계약 상태 확인 modifier
    modifier onlyValidStatus(ContractStatus _status) {
        require(contractStatus == _status, "잘못된 상태입니다");
        _;
    }

    // 임차인 또는 임대인 modifier
    modifier onlyTenantOrLessor() {
        require(msg.sender == tenant || msg.sender == lessor, "임차인 또는 임대인만 호출할 수 있습니다");
        _;
    }

    // 임대인이 매물 정보를 입력하고 계약서를 작성하여 임차인에게 보내는 함수
    function sendContract(address _tenant, uint _rentAmount, uint _deposit, uint _paymentSchedule, string memory _propertyAddress, string memory _specialTerms) public {
        require(msg.sender == lessor, "임대인만 계약을 보낼 수 있습니다");
        require(_tenant != address(0), "잘못된 임차인 주소입니다");

        tenant = _tenant;
        rentAmount = _rentAmount;
        agreement = new Agreement(_deposit, _rentAmount, _paymentSchedule, _propertyAddress, _specialTerms);
        contractStatus = ContractStatus.Initiated;
        emit ContractInitiated(lessor, _rentAmount, _propertyAddress);
    }

    // 임차인이 계약을 수락하는 함수
    function acceptLeaseContract() public {
        require(msg.sender == tenant, "임차인만 계약을 수락할 수 있습니다");
        require(tenant != address(0), "계약이 아직 수락되지 않았습니다");

        contractStatus = ContractStatus.Accepted;
        emit ContractAccepted(tenant, rentAmount);
    }

    // 임차인 또는 임대인이 계약을 서명하는 함수
    function signContract(bytes32 _signatureHash) public {
        require(msg.sender == tenant || msg.sender == lessor, "임차인 또는 임대인만 계약에 서명할 수 있습니다");
        require(contractStatus == ContractStatus.Accepted, "계약이 아직 수락되지 않았습니다");
        require(!hasSigned[msg.sender], "서명자가 이미 계약을 서명했습니다");

        // 서명에 대한 해시값 저장
        hasSigned[msg.sender] = true;
        emit ContractSigned(msg.sender, msg.sender == tenant);

        if (hasSigned[tenant] && hasSigned[lessor]) {
            completeTransaction();
        }
    }

    // 계약을 완료하는 함수
    function completeTransaction() private {
        require(hasSigned[tenant] && hasSigned[lessor], "임차인과 임대인 모두 계약에 서명해야 합니다");
        require(!contractCompleted, "계약이 이미 완료되었습니다");

        contractCompleted = true;
        contractStatus = ContractStatus.Completed;

        emit ContractCompleted(lessor, tenant, rentAmount);
    }

    // 계약 취소하는 함수
    function cancelContract(string memory _reason) public {
        require(msg.sender == lessor || msg.sender == tenant, "임대인 또는 임차인만 계약을 취소할 수 있습니다");
        require(contractStatus != ContractStatus.Completed && contractStatus != ContractStatus.Canceled, "이미 완료된 계약이거나 취소된 계약입니다");

        contractStatus = ContractStatus.Canceled;
        emit ContractCanceled(msg.sender, _reason);
    }
}
