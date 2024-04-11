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
    private final LessorRepository lessorRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Tenant> tenantOptional = tenantRepository.findByUsername(username);
        if (tenantOptional.isPresent()) {
            Tenant tenant = tenantOptional.get();
            return User.builder()
                    .username(tenant.getUsername())
                    .password(tenant.getEncryptedPwd())
                    .authorities(new SimpleGrantedAuthority("TENANT"))
                    .build();
        }

        Optional<Lessor> lessorOptional = lessorRepository.findByUsername(username);
        if (lessorOptional.isPresent()) {
            Lessor lessor = lessorOptional.get();
            return User.builder()
                    .username(lessor.getUsername())
                    .password(lessor.getEncryptedPwd())
                    .authorities(new SimpleGrantedAuthority("LESSOR"))
                    .build();
        }

        throw new UsernameNotFoundException("해당하는 사용자가 없습니다: " + username);
    }

    public Long findUserIdByUsername(String username) {
        Optional<Tenant> tenantOptional = tenantRepository.findByUsername(username);
        if (tenantOptional.isPresent()) {
            return tenantOptional.get().getTenantId();
        }

        Optional<Lessor> lessorOptional = lessorRepository.findByUsername(username);
        if (lessorOptional.isPresent()) {
            return lessorOptional.get().getLessorId();
        }

        throw new UsernameNotFoundException("해당하는 사용자가 없습니다: " + username);
    }

    public String findNameByUsername(String username) {
        Optional<Tenant> tenantOptional = tenantRepository.findByUsername(username);
        if (tenantOptional.isPresent()) {
            return tenantOptional.get().getName();
        }

        Optional<Lessor> lessorOptional = lessorRepository.findByUsername(username);
        if (lessorOptional.isPresent()) {
            return lessorOptional.get().getName();
        }

        throw new UsernameNotFoundException("해당하는 사용자가 없습니다: " + username);
    }
    }
}
