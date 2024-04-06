package com.yageum.fintech.domain.tenant.infrastructure;

import com.yageum.fintech.domain.tenant.dto.response.GetTenantResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByEmail(String email);

    Boolean existsById(String tenantId);

    Optional<Tenant> findByTenantId(Long tenantId);

    default GetTenantResponseDto findUserResponseByUserId(Long userId) {
        Optional<Tenant> userEntityOptional = findById(userId);
        Tenant tenant = userEntityOptional.get();
        return GetTenantResponseDto.from(tenant);
    }

    default GetTenantResponseDto findUserResponseByEmail(String email) {
        Optional<Tenant> userEntityOptional = findByEmail(email);
        Tenant tenant = userEntityOptional.get();
        return GetTenantResponseDto.from(tenant);
    }
}
