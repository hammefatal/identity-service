package com.hammefatal.digitalworkshop.identity_service.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByUsername(String username);
    
    Optional<UserEntity> findByEmail(String email);
    
    List<UserEntity> findByAccountStatus(UserEntity.AccountStatus status);
    
    List<UserEntity> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<UserEntity> findByLastNameContainingIgnoreCase(String lastName);
    
    List<UserEntity> findByEmailContainingIgnoreCase(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}