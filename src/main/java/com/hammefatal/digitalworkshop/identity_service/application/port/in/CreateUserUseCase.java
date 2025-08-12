package com.hammefatal.digitalworkshop.identity_service.application.port.in;

import com.hammefatal.digitalworkshop.identity_service.domain.User;

public interface CreateUserUseCase {
    
    User createUser(CreateUserCommand command);
    
    record CreateUserCommand(
        String username,
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber
    ) {
        public CreateUserCommand {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("First name cannot be empty");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("Last name cannot be empty");
            }
        }
    }
}