package com.hammefatal.digitalworkshop.identity_service.application.port.in;

import com.hammefatal.digitalworkshop.identity_service.domain.User;
import java.time.LocalDate;

public interface UpdateUserUseCase {
    
    User updateUser(UpdateUserCommand command);
    
    User updateUserProfile(UpdateUserProfileCommand command);
    
    User updateUserStatus(UpdateUserStatusCommand command);
    
    User updateUserPassword(UpdateUserPasswordCommand command);
    
    record UpdateUserCommand(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate dateOfBirth,
        String profileImageUrl
    ) {
        public UpdateUserCommand {
            if (id == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
        }
    }
    
    record UpdateUserProfileCommand(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate dateOfBirth
    ) {
        public UpdateUserProfileCommand {
            if (id == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
        }
    }
    
    record UpdateUserStatusCommand(
        Long id,
        User.AccountStatus accountStatus
    ) {
        public UpdateUserStatusCommand {
            if (id == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (accountStatus == null) {
                throw new IllegalArgumentException("Account status cannot be null");
            }
        }
    }
    
    record UpdateUserPasswordCommand(
        Long id,
        String currentPassword,
        String newPassword
    ) {
        public UpdateUserPasswordCommand {
            if (id == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Current password cannot be empty");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("New password cannot be empty");
            }
        }
    }
}