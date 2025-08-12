package com.hammefatal.digitalworkshop.identity_service.application.service;

import com.hammefatal.digitalworkshop.identity_service.application.port.in.*;
import com.hammefatal.digitalworkshop.identity_service.application.port.out.UserRepository;
import com.hammefatal.digitalworkshop.identity_service.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements CreateUserUseCase, GetUserUseCase, UpdateUserUseCase, DeleteUserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new IllegalArgumentException("Username already exists: " + command.username());
        }
        
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already exists: " + command.email());
        }

        User user = new User(
            command.username(),
            command.email(),
            hashPassword(command.password()),
            command.firstName(),
            command.lastName()
        );
        
        if (command.phoneNumber() != null && !command.phoneNumber().trim().isEmpty()) {
            user.setPhoneNumber(command.phoneNumber());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByStatus(User.AccountStatus status) {
        return userRepository.findByAccountStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsersByName(String searchTerm) {
        List<User> byFirstName = userRepository.findByFirstNameContaining(searchTerm);
        List<User> byLastName = userRepository.findByLastNameContaining(searchTerm);
        
        byFirstName.addAll(byLastName);
        return byFirstName.stream().distinct().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsersByEmail(String searchTerm) {
        return userRepository.findByEmailContaining(searchTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalUserCount() {
        return userRepository.count();
    }

    @Override
    public User updateUser(UpdateUserCommand command) {
        User user = userRepository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + command.id()));

        if (command.firstName() != null) {
            user.setFirstName(command.firstName());
        }
        if (command.lastName() != null) {
            user.setLastName(command.lastName());
        }
        if (command.phoneNumber() != null) {
            user.setPhoneNumber(command.phoneNumber());
        }
        if (command.dateOfBirth() != null) {
            user.setDateOfBirth(command.dateOfBirth());
        }
        if (command.profileImageUrl() != null) {
            user.setProfileImageUrl(command.profileImageUrl());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfile(UpdateUserProfileCommand command) {
        User user = userRepository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + command.id()));

        if (command.firstName() != null) {
            user.setFirstName(command.firstName());
        }
        if (command.lastName() != null) {
            user.setLastName(command.lastName());
        }
        if (command.phoneNumber() != null) {
            user.setPhoneNumber(command.phoneNumber());
        }
        if (command.dateOfBirth() != null) {
            user.setDateOfBirth(command.dateOfBirth());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User updateUserStatus(UpdateUserStatusCommand command) {
        User user = userRepository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + command.id()));

        user.setAccountStatus(command.accountStatus());
        user.setUpdatedAt(LocalDateTime.now());
        
        if (command.accountStatus() == User.AccountStatus.ACTIVE) {
            user.resetFailedLoginAttempts();
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUserPassword(UpdateUserPasswordCommand command) {
        User user = userRepository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + command.id()));

        if (!verifyPassword(command.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(hashPassword(command.newPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.resetFailedLoginAttempts();

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        userRepository.delete(user);
    }

    @Override
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        userRepository.delete(user);
    }

    @Override
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        
        user.setAccountStatus(User.AccountStatus.INACTIVE);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private String hashPassword(String password) {
        // TODO: Implement proper password hashing (BCrypt, Argon2, etc.)
        return "hashed_" + password;
    }

    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        // TODO: Implement proper password verification
        return hashedPassword.equals("hashed_" + rawPassword);
    }
}