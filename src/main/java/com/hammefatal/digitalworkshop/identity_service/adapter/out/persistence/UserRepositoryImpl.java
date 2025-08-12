package com.hammefatal.digitalworkshop.identity_service.adapter.out.persistence;

import com.hammefatal.digitalworkshop.identity_service.application.port.out.UserRepository;
import com.hammefatal.digitalworkshop.identity_service.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByAccountStatus(User.AccountStatus status) {
        UserEntity.AccountStatus entityStatus = UserEntity.AccountStatus.valueOf(status.name());
        return userJpaRepository.findByAccountStatus(entityStatus).stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByFirstNameContaining(String firstName) {
        return userJpaRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByLastNameContaining(String lastName) {
        return userJpaRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByEmailContaining(String email) {
        return userJpaRepository.findByEmailContainingIgnoreCase(email).stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public long count() {
        return userJpaRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public void delete(User user) {
        UserEntity entity = userMapper.toEntity(user);
        userJpaRepository.delete(entity);
    }
}