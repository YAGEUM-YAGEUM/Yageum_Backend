// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

    enum Build_DSG {
        log, //0
        ironframe_concrete, //1
        rebar_concrete, //2
        stone, //3
        pc, //4
        wood_based, //5
        brick, //6
        cement_brick, //7
        ocher, //8
        ironframe, //9
        steel_house, //10
        reinforced_concrete, //11
        wood, //12
        cement_block, //13
        light_steel_frame, //14
        iron_pipe, //15
        lime_mud_others //16
    }

    enum Contract_TYPE {
        new_contract,
        renewal,
        request_renewal
    }

    enum ContractStatus {
        Initiated, // 초기화
        Accepted,  // 수락
        Signed,    // 서명완료
        Completed, // 완료
        Canceled   // 취소
    }
