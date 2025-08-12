package com.hammefatal.digitalworkshop.identity_service.application.port.in;

import com.hammefatal.digitalworkshop.identity_service.domain.User;
import java.util.List;
import java.util.Optional;

public interface GetUserUseCase {
    
    Optional<User> getUserById(Long id);
    
    Optional<User> getUserByUsername(String username);
    
    Optional<User> getUserByEmail(String email);
    
    List<User> getAllUsers();
    
    List<User> getUsersByStatus(User.AccountStatus status);
    
    List<User> searchUsersByName(String searchTerm);
    
    List<User> searchUsersByEmail(String searchTerm);
    
    long getTotalUserCount();
}