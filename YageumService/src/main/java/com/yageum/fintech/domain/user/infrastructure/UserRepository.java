package com.yageum.fintech.domain.user.infrastructure;

import com.yageum.fintech.domain.user.dto.response.GetUserResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<UserEntity> findById(Long userId);

    default GetUserResponseDto findUserResponseByUserId(Long userId) {
        Optional<UserEntity> userEntityOptional = findById(userId);
        UserEntity userEntity = userEntityOptional.get();
        return GetUserResponseDto.from(userEntity);
    }

    default GetUserResponseDto findUserResponseByEmail(String email) {
        Optional<UserEntity> userEntityOptional = findByEmail(email);
        UserEntity userEntity = userEntityOptional.get();
        return GetUserResponseDto.from(userEntity);
    }
}
