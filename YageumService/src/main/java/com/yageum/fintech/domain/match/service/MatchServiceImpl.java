package com.yageum.fintech.domain.match.service;

import com.yageum.fintech.domain.house.infrastructure.House;
import com.yageum.fintech.domain.house.infrastructure.HouseRepository;
import com.yageum.fintech.domain.match.dto.request.UpdateMatchStateDto;
import com.yageum.fintech.domain.match.dto.response.MatchHouseResponseDto;
import com.yageum.fintech.domain.match.dto.response.MatchTenantResponseDto;
import com.yageum.fintech.domain.match.infrastructure.Matching;
import com.yageum.fintech.domain.match.infrastructure.MatchRepository;
import com.yageum.fintech.domain.match.infrastructure.MatchState;
import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
import com.yageum.fintech.domain.tenant.infrastructure.TenantRepository;
import com.yageum.fintech.global.model.Exception.ExceptionList;
import com.yageum.fintech.global.model.Exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService{

    private final TenantRepository tenantRepository;
    private final HouseRepository houseRepository;
    private final MatchRepository matchRepository;

    @Override
    public void createMatch(Long tenantId, Long houseId) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_TENANT));

        House house = houseRepository.findByHouseId(houseId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE));

        Matching matching = Matching.builder()
                .tenant(tenant)
                .house(house)
                .state(MatchState.PREREQUEST)
                .build();

        matchRepository.save(matching);
    }

    @Override
    public List<MatchHouseResponseDto> getMatchesByTenantId(Long tenantId) {
        Tenant tenant = tenantRepository.findByTenantId(tenantId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_TENANT));

        List<Matching> matchings = matchRepository.findByTenantTenantId(tenantId);

        List<MatchHouseResponseDto> responseMatchHouseList = new ArrayList<>();
        for (Matching matching : matchings) {
            House house = matching.getHouse();
            MatchHouseResponseDto responseDto = MatchHouseResponseDto.from(house, matching);
            responseMatchHouseList.add(responseDto);
        }

        if (responseMatchHouseList.isEmpty()) throw new NonExistentException(ExceptionList.NON_EXISTENT_HOUSELIST);
        return responseMatchHouseList;
    }

    @Override
    public List<MatchTenantResponseDto> getTenantsByHouseId(Long houseId) {
        House house = houseRepository.findByHouseId(houseId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_HOUSE));
        List<Matching> matchings = matchRepository.findByHouseHouseId(houseId);

        List<MatchTenantResponseDto> responseMatchHouseList = new ArrayList<>();
        for (Matching matching : matchings) {
            Tenant tenant = matching.getTenant();
            MatchTenantResponseDto responseDto = MatchTenantResponseDto.from(tenant, matching);
            responseMatchHouseList.add(responseDto);
        }
        if (responseMatchHouseList.isEmpty()) throw new NonExistentException(ExceptionList.NON_EXISTENT_TENANTLIST);
        return responseMatchHouseList;

    }

    @Override
    public void updateMatchState(Long matchId, UpdateMatchStateDto updateDto) {
        Matching matching = matchRepository.findById(matchId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_MATCH));

        matching.update(updateDto);
    }

    @Override
    public void deleteMatch(Long matchId) {
        Matching matching = matchRepository.findById(matchId)
                .orElseThrow(() -> new NonExistentException(ExceptionList.NON_EXISTENT_MATCH));

        matchRepository.deleteById(matchId);
    }
}
