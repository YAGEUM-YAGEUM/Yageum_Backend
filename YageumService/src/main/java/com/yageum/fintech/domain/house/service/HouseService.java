package com.yageum.fintech.domain.house.service;

import com.yageum.fintech.domain.house.dto.request.HouseRequestDto;
import com.yageum.fintech.domain.house.dto.response.HouseResponseDto;

import java.util.List;

public interface HouseService {

    void createHouse(Long lessorId, HouseRequestDto houseRequestDto);

    void updateHouse(Long houseId, HouseRequestDto houseRequestDto);

    List<HouseResponseDto> getHousesByLessorId(Long lessorId);

    void deleteHouse(Long houseId);
}
