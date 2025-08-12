package com.hammefatal.digitalworkshop.identity_service.adapter.in.web;

import com.hammefatal.digitalworkshop.identity_service.application.port.in.*;
import com.hammefatal.digitalworkshop.identity_service.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, 
                         GetUserUseCase getUserUseCase,
                         UpdateUserUseCase updateUserUseCase, 
                         DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = getUserUseCase.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return getUserUseCase.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return getUserUseCase.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return getUserUseCase.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable User.AccountStatus status) {
        List<User> users = getUserUseCase.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String searchTerm) {
        List<User> users = getUserUseCase.searchUsersByName(searchTerm);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/email")
    public ResponseEntity<List<User>> searchUsersByEmail(@RequestParam String searchTerm) {
        List<User> users = getUserUseCase.searchUsersByEmail(searchTerm);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUserCount() {
        long count = getUserUseCase.getTotalUserCount();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        try {
            CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand(
                    request.username(),
                    request.email(),
                    request.password(),
                    request.firstName(),
                    request.lastName(),
                    request.phoneNumber()
            );
            User createdUser = createUserUseCase.createUser(command);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        try {
            UpdateUserUseCase.UpdateUserCommand command = new UpdateUserUseCase.UpdateUserCommand(
                    id,
                    request.firstName(),
                    request.lastName(),
                    request.phoneNumber(),
                    request.dateOfBirth(),
                    request.profileImageUrl()
            );
            User updatedUser = updateUserUseCase.updateUser(command);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long id, @RequestBody UpdateUserProfileRequest request) {
        try {
            UpdateUserUseCase.UpdateUserProfileCommand command = new UpdateUserUseCase.UpdateUserProfileCommand(
                    id,
                    request.firstName(),
                    request.lastName(),
                    request.phoneNumber(),
                    request.dateOfBirth()
            );
            User updatedUser = updateUserUseCase.updateUserProfile(command);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<User> updateUserStatus(@PathVariable Long id, @RequestBody UpdateUserStatusRequest request) {
        try {
            UpdateUserUseCase.UpdateUserStatusCommand command = new UpdateUserUseCase.UpdateUserStatusCommand(
                    id,
                    request.accountStatus()
            );
            User updatedUser = updateUserUseCase.updateUserStatus(command);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<User> updateUserPassword(@PathVariable Long id, @RequestBody UpdateUserPasswordRequest request) {
        try {
            UpdateUserUseCase.UpdateUserPasswordCommand command = new UpdateUserUseCase.UpdateUserPasswordCommand(
                    id,
                    request.currentPassword(),
                    request.newPassword()
            );
            User updatedUser = updateUserUseCase.updateUserPassword(command);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            deleteUserUseCase.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        try {
            deleteUserUseCase.deleteUserByUsername(username);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        try {
            deleteUserUseCase.deleteUserByEmail(email);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id) {
        try {
            deleteUserUseCase.softDeleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public record CreateUserRequest(
            String username,
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber
    ) {}

    public record UpdateUserRequest(
            String firstName,
            String lastName,
            String phoneNumber,
            LocalDate dateOfBirth,
            String profileImageUrl
    ) {}

    public record UpdateUserProfileRequest(
            String firstName,
            String lastName,
            String phoneNumber,
            LocalDate dateOfBirth
    ) {}

    public record UpdateUserStatusRequest(
            User.AccountStatus accountStatus
    ) {}

    public record UpdateUserPasswordRequest(
            String currentPassword,
            String newPassword
    ) {}
}
