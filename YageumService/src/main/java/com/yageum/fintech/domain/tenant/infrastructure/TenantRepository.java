package com.yageum.fintech.domain.tenant.infrastructure;

import com.yageum.fintech.domain.tenant.dto.response.GetTenantResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<TenantEntity, Long> {

    Optional<TenantEntity> findByEmail(String email);

    Boolean existsById(String userId);

    Optional<TenantEntity> findById(Long userId);

    default GetTenantResponseDto findUserResponseByUserId(Long userId) {
        Optional<TenantEntity> userEntityOptional = findById(userId);
        TenantEntity tenantEntity = userEntityOptional.get();
        return GetTenantResponseDto.from(tenantEntity);
    }

    default GetTenantResponseDto findUserResponseByEmail(String email) {
        Optional<TenantEntity> userEntityOptional = findByEmail(email);
        TenantEntity tenantEntity = userEntityOptional.get();
        return GetTenantResponseDto.from(tenantEntity);
    }
}
