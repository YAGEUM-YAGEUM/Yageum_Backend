package com.yageum.fintech.domain.tenant.service;

import com.yageum.fintech.domain.tenant.infrastructure.TenantEntity;
import com.yageum.fintech.domain.tenant.infrastructure.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final TenantRepository tenantRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<TenantEntity> findOne = tenantRepository.findByEmail(email);
        TenantEntity tenantEntity = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다"));

        return User.builder()
                .username(tenantEntity.getName())
                .password(tenantEntity.getEncryptedPwd())
                .authorities(new SimpleGrantedAuthority("ADMIN"))
                .build();
    }

    public Long findUserIdByEmail(String email) {
        Optional<TenantEntity> findOne = tenantRepository.findByEmail(email);
        TenantEntity tenantEntity = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다"));

        return tenantEntity.getId();
    }

}
