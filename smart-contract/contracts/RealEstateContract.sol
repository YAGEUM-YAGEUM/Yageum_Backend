pragma solidity ^0.8.0;

contract RealEstateContract {
    address public lessor; // 임대인
    address public tenant; // 임차인
    uint public rentAmount; // 임대료
    string public propertyAddress; // 부동산 주소
    string public specialTerms; // 특약 사항
    mapping(address => bool) public hasSigned; // 서명 여부 저장
    bool public contractCompleted; // 계약 완료 여부

    event ContractInitiated(address indexed _lessor, uint _rentAmount, string _propertyAddress, string _specialTerms);
    event ContractAccepted(address indexed _tenant);
    event ContractSigned(address indexed _signer);
    event ContractCompleted(address indexed _lessor, address indexed _tenant);

    constructor(uint _rentAmount, string memory _propertyAddress, string memory _specialTerms) {
        lessor = msg.sender;
        rentAmount = _rentAmount;
        propertyAddress = _propertyAddress;
        specialTerms = _specialTerms;
        contractCompleted = false;
        emit ContractInitiated(msg.sender, _rentAmount, _propertyAddress, _specialTerms);
    }

    // 임대인이 매물 정보를 입력하고 계약서를 작성하여 임차인에게 보내는 함수
    function sendContract(string memory _propertyAddress, string memory _specialTerms) public {
        require(msg.sender == lessor, "임대인만 계약을 보낼 수 있습니다");
        require(tenant != address(0), "임차인 주소가 설정되지 않았습니다");

        propertyAddress = _propertyAddress;
        specialTerms = _specialTerms;
        emit ContractInitiated(lessor, rentAmount, _propertyAddress, _specialTerms);
    }


    // 임차인이 계약을 수락하고 계약금을 지불하는 함수
    function acceptLeaseContract() public payable {
        require(msg.value == rentAmount * 2, "지불 금액이 올바르지 않습니다");
        require(tenant == address(0), "계약이 이미 수락되었습니다");

        tenant = msg.sender;
        emit ContractAccepted(msg.sender);
    }

    // 임차인 또는 임대인이 계약을 서명하는 함수
    function signContract() public {
        require(msg.sender == tenant || msg.sender == lessor, "임차인 또는 임대인만 계약에 서명할 수 있습니다");
        require(tenant != address(0) && lessor != address(0), "계약이 아직 수락되지 않았습니다");
        require(!hasSigned[msg.sender], "서명자가 이미 계약을 서명했습니다");

        emit ContractSigned(msg.sender);

        hasSigned[msg.sender] = true;
        emit ContractSigned(msg.sender);

        if (hasSigned[tenant] && hasSigned[lessor]) {
            completeTransaction();
        }
    }

    // 계약을 완료하는 함수
    function completeTransaction() private {
        require(tenantSigned && lessorSigned, "임차인과 임대인 모두 계약에 서명해야 합니다");
        require(!contractCompleted, "계약이 이미 완료되었습니다");

        contractCompleted = true;

        // 임대료 송금
        payable(lessor).transfer(rentAmount);

        // 계약금 환불
        payable(tenant).transfer(address(this).balance - rentAmount);

        emit ContractCompleted(lessor, tenant);
    }

}