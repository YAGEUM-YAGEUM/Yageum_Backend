package com.yageum.fintech.domain.house.service;

import com.yageum.fintech.domain.house.dto.request.HouseOptionDto;
import com.yageum.fintech.domain.house.dto.request.HouseRequestDto;
import com.yageum.fintech.domain.house.dto.response.HouseOptionResponseDto;
import com.yageum.fintech.domain.house.dto.response.HouseResponseDto;
import com.yageum.fintech.domain.house.infrastructure.*;
import com.yageum.fintech.domain.lessor.infrastructure.Lessor;
import com.yageum.fintech.domain.lessor.infrastructure.LessorRepository;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService{

    private final LessorRepository lessorRepository;
    private final HouseRepository houseRepository;
    private final HouseOptionRepository houseOptionRepository;

    @Transactional
    @Override
    public void createHouse(Long lessorId, HouseRequestDto houseRequestDto) {
        Lessor lessor = lessorRepository.findByLessorId(lessorId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_LESSOR));

        houseRepository.save(houseRequestDto.toEntity(lessor));
    }

    @Transactional
    @Override
    public void updateHouse(Long houseId, HouseRequestDto houseRequestDto) {
        House house = houseRepository.findByHouseId(houseId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE));

        house.update(houseRequestDto);
    }

    @Override
    public List<HouseResponseDto> getHousesByLessorId(Long lessorId) {
        Lessor lessor = lessorRepository.findByLessorId(lessorId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_LESSOR));
        List<House> houses = houseRepository.findByLessor_LessorId(lessorId);

        List<HouseResponseDto> houseList =
                houses.stream()
                .map(house -> HouseResponseDto.from(lessor, house))
                .collect(Collectors.toList());

        if(houseList.isEmpty()) throw new NonExistentException(ExceptionList.NON_EXISTENT_HOUSELIST);
        return houseList;
    }

    @Transactional
    @Override
    public void deleteHouse(Long houseId) {
        House house = houseRepository.findByHouseId(houseId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE));
        houseRepository.deleteByHouseId(houseId);
    }

    @Override
    public boolean existsById(Long houseId) {
        return houseRepository.existsById(houseId);
    }

    @Transactional
    @Override
    public void createHouseOption(Long houseId, HouseOptionDto houseOptionDto) {
        House house = houseRepository.findByHouseId(houseId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE));

        HouseOption houseOption = houseOptionDto.toEntity(house);
        houseOptionRepository.save(houseOption);
    }

    @Transactional
    @Override
    public void updateHouseOption(Long houseId, HouseOptionDto houseOptionDto) {
        House house = houseRepository.findByHouseId(houseId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE));

        HouseOption existingHouseOption = houseOptionRepository.findByHouseId(houseId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSEOPTION));

        existingHouseOption.update(houseOptionDto);
    }

    @Override
    public HouseOptionResponseDto getHouseOptionByHouseId(Long houseId) {
        HouseOption houseOption = houseOptionRepository.findByHouseId(houseId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSEOPTION));

        return HouseOptionResponseDto.from(houseOption);

    }

    @Transactional
    @Override
    public void updateDealStatus(Long houseId, DealStatus newDealStatus) {
        House house = houseRepository.findByHouseId(houseId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE));

        house.updateDealStatus(newDealStatus);
    }

    @Override
    public boolean isHouseDealCompleted(Long houseId) {
        House house = houseRepository.findByHouseId(houseId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE));

        // 거래 상태 확인 => 거래 완료 시 true, 아닐 시 false
        return house.getDealStatus() == DealStatus.DEAL_COMPLETED;
    }
}
