package com.hammefatal.digitalworkshop.identity_service.application.port.in;

public interface DeleteUserUseCase {
    
    void deleteUser(Long id);
    
    void deleteUserByUsername(String username);
    
    void deleteUserByEmail(String email);
    
    void softDeleteUser(Long id);
}