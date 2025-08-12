package com.hammefatal.digitalworkshop.identity_service.adapter.out.persistence;

import com.hammefatal.digitalworkshop.identity_service.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setDateOfBirth(user.getDateOfBirth());
        entity.setProfileImageUrl(user.getProfileImageUrl());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setPhoneVerified(user.isPhoneVerified());
        
        if (user.getAccountStatus() != null) {
            entity.setAccountStatus(UserEntity.AccountStatus.valueOf(user.getAccountStatus().name()));
        }
        
        entity.setFailedLoginAttempts(user.getFailedLoginAttempts());
        entity.setLastLoginAt(user.getLastLoginAt());
        entity.setPasswordChangedAt(user.getPasswordChangedAt());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setCreatedBy(user.getCreatedBy());
        entity.setUpdatedBy(user.getUpdatedBy());

        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setPasswordHash(entity.getPasswordHash());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setPhoneNumber(entity.getPhoneNumber());
        user.setDateOfBirth(entity.getDateOfBirth());
        user.setProfileImageUrl(entity.getProfileImageUrl());
        user.setEmailVerified(entity.isEmailVerified());
        user.setPhoneVerified(entity.isPhoneVerified());
        
        if (entity.getAccountStatus() != null) {
            user.setAccountStatus(User.AccountStatus.valueOf(entity.getAccountStatus().name()));
        }
        
        user.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        user.setLastLoginAt(entity.getLastLoginAt());
        user.setPasswordChangedAt(entity.getPasswordChangedAt());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setCreatedBy(entity.getCreatedBy());
        user.setUpdatedBy(entity.getUpdatedBy());

        return user;
    }
}