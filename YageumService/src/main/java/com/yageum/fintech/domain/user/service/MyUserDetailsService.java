package com.yageum.fintech.domain.user.service;

import com.yageum.fintech.domain.user.infrastructure.UserEntity;
import com.yageum.fintech.domain.user.infrastructure.UserRepository;
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

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> findOne = userRepository.findByEmail(email);
        UserEntity userEntity = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다"));

        return User.builder()
                .username(userEntity.getName())
                .password(userEntity.getEncryptedPwd())
                .authorities(new SimpleGrantedAuthority("ADMIN"))
                .build();
    }

    public Long findUserIdByEmail(String email) {
        Optional<UserEntity> findOne = userRepository.findByEmail(email);
        UserEntity userEntity = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다"));

        return userEntity.getId();
    }

}
