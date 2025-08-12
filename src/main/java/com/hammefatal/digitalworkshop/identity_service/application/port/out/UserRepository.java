package com.hammefatal.digitalworkshop.identity_service.application.port.out;

import com.hammefatal.digitalworkshop.identity_service.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    User save(User user);
    
    Optional<User> findById(Long id);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findAll();
    
    List<User> findByAccountStatus(User.AccountStatus status);
    
    List<User> findByFirstNameContaining(String firstName);
    
    List<User> findByLastNameContaining(String lastName);
    
    List<User> findByEmailContaining(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    long count();
    
    void deleteById(Long id);
    
    void delete(User user);
}