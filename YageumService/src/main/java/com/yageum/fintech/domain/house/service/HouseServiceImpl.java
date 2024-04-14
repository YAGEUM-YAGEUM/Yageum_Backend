package com.yageum.fintech.domain.house.service;

import com.yageum.fintech.domain.house.dto.request.HouseRequestDto;
import com.yageum.fintech.domain.house.dto.response.HouseResponseDto;
import com.yageum.fintech.domain.house.infrastructure.House;
import com.yageum.fintech.domain.house.infrastructure.HouseRepository;
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
@Transactional
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService{

    private final LessorRepository lessorRepository;
    private final HouseRepository houseRepository;

    @Override
    public void createHouse(Long lessorId, HouseRequestDto houseRequestDto) {
        Lessor lessor = lessorRepository.findByLessorId(lessorId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_LESSOR));

        houseRepository.save(houseRequestDto.toEntity(lessor));
    }

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

    @Override
    public void deleteHouse(Long houseId) {
        House house = houseRepository.findByHouseId(houseId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE));
        houseRepository.deleteByHouseId(houseId);
    }
}
