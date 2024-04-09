package com.yageum.fintech.domain.auth.service;

import com.yageum.fintech.domain.tenant.infrastructure.Tenant;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Tenant> findOne = tenantRepository.findByUsername(username);
        Tenant tenant = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 임차인입니다"));

        return User.builder()
                .username(tenant.getUsername()) //아이디
                .password(tenant.getEncryptedPwd()) //비번
                .authorities(new SimpleGrantedAuthority("ADMIN"))
                .build();
    }

    public Long findUserIdByUsername(String username) {
        Optional<Tenant> findOne = tenantRepository.findByUsername(username);
        Tenant tenant = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다"));

        return tenant.getTenantId();
    }

    public String findNameByUsername(String username) {
        Optional<Tenant> findOne = tenantRepository.findByUsername(username);
        Tenant tenant = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다"));

        return tenant.getName();
    }
}
