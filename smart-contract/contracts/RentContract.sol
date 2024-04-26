// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;
import "./EnumDeclaration.sol";

contract RentContact{

    struct Room_Info{
        string locatin_addr; //소재지 (도로명 주소)
        string land_designation; //토지 지목 (대)
        string land_area; //토지 면적
        Build_DSG build_dsg; //건물 구조, 용도
        string building_area; //건물 면적
        string specific_room; //임차할 부분 (동, 층, 호 정확히 기재)
        string room_area; //임차할 부분 면적
        Contract_TYPE contract_type; //계약의 종류 (신규, 합의 재계약, 개약갱신요구권 행사)
        bool non_payment_tax; //미납 국세 및 지방세 여부
        bool confirmation_seniority; //선순위 확정일자 현황
    }

    struct Contract_Details_Basic{
        string deposit_kor; //보증금 금 ~ 원 정
        uint deposit_num; //보증금
        string down_payment_kor; //계약금
        uint down_payment_num;
        string interim_payment_kor; //중도금
        uint interim_payment_num;
        string interim_payment_date; //중도금 지불일 YYYY.MM.DD
        string balance_kor; //잔금
        uint balance_num;
        string balance_date; //잔금 지불일 YYYY.MM.DD
        string monthly_payment_kor; //월세
        string deposit_account; //입금 계좌
    }
    //mapping 사용하여 관리 예정 - 아마도?
    //관리비 정액인 경우
    struct Contract_Details_Flat_Fee{
        string total_amount_kor;
        uint total_amount_num;

        string normal_fee_kor; //일반 관리비
        uint normal_fee_num;
        string electricity_fee_kor; //전기세
        uint electricity_fee_num;
        string water_fee_kor; //수도세
        uint water_fee_num;
        string gas_fee_kor; //가스 사용료
        uint gas_fee_num;
        string heating_fee_kor; //난방비
        uint heating_fee_num;
        string internet_fee_kor; //인터넷 사용료
        uint internet_fee_num;
        string tv_fee_kor; //tv 사용료
        uint tv_fee_num;
        string others_kor; //기타 사용료
        uint others_num;
    }
    //관리비 정액이 아닌경우
    struct Contract_Details_nonFlat_Fee{
        string details; //관리비 항목 및 산정방식 기재 (세대별 사용량 비례, 세대수 비례 등)
    }

    struct Contract_Details_Others{
        string delivery_date; //임차주택 인도일 YYYY.MM.DD
        string lease_period; //임대차 기간 YYYY.MM.DD
        string repair_details; //수리 필요 시설 유무 및 내용. 값 없으면 없음.
        string repair_finish_date; // 수리 완료 시기. 잔금지급 기일과 같지 않은 경우 기타에 날짜와 함께 체크
        string repair_promise; //수리 필요시설 있음 체크 & 값 없으면 수리비 임차인이 임대인에게 지급하여야 할 보증금 또는 차임에서 공제, 값 있으면 기타 체크 후 내용
        string repair_while_lessor; // 계약 존속 중 임차주택 수리 및 비용부담 관련 임대인 부담
        string repair_while_tenant; // 임차인 부담
    }

    struct Special_Contract{
        string addr_change_date; // 전입신고 및 확정일자 데드라인 YYYY.MM.DD
        uint unpaid_tax; // 임대차 체결 시 금전 기타 물건을 포기하지 않고 임대차계약 해제할 수 있는 미납 및 체납 국세,지방세 초과액 기준
        bool reconciliaton_agreement; // 주택임재차계약 관련 분쟁 발생 시 법원 소 제기 전 조정 신청 동의 여부
        string rebuilding_plan; // 값 없으면 없음으로 체크, 있는 경우 공사시기와 소요기간을 YYYY.MM.DD.PP로 표기
        bool detailed_addr_agreement; // 상세주소가 없는 경우 임차인 상세주소 부여 신청에 대한 소유자 동의 여부
        string others; // 기타 특약 작성
    }

    struct lessor_tenant_info{
        //계약체결일
        string tenant_date; // YYYY.MM.DD
        //lessor 정보
        string lessor_addr;
        string lessor_id;
        string lessor_pn;
        string lessor_name;
        string lessor_agent_addr;
        string lessor_agent_id;
        string lessor_agent_name;
        string lessor_sign;
        //tenant 정보
        string tenant_addr;
        string tenant_id;
        string tenant_pn;
        string tenant_name;
        string tenant_agent_addr;
        string tenant_agent_id;
        string tenant_agent_name;
        string tenant_sign;
    }



}