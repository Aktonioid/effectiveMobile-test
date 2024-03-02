package com.effectiveMobile.client.infrastructure.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.effectiveMobile.client.core.dtos.UserEmailDto;
import com.effectiveMobile.client.core.dtos.UserModelDto;
import com.effectiveMobile.client.core.dtos.UserPhoneDto;
import com.effectiveMobile.client.core.mappers.UserEmailMapper;
import com.effectiveMobile.client.core.mappers.UserModelMapper;
import com.effectiveMobile.client.core.mappers.UserPhoneMapper;
import com.effectiveMobile.client.core.models.UserModel;
import com.effectiveMobile.client.core.repositories.IUserRepo;
import com.effectiveMobile.client.core.service.IUserService;

@Service
@EnableAsync
public class UserService implements IUserService{

    @Autowired
    IUserRepo userRepo;

    @Override
    public UserDetailsService loadUserDetailsById() 
    {
        return this::forUserDetailsService;
    }

    private UserModel forUserDetailsService(String userId)
    {
        return userRepo.getUserById(UUID.fromString(userId));
    }

    @Async
    @Override
    public CompletableFuture<List<UserModelDto>> getAllUsers() {
        List<UserModelDto> users = userRepo.getAllUsers()
                                    .stream()
                                    .map(UserModelMapper::asDto)
                                    .collect(Collectors.toList());

        return CompletableFuture.completedFuture(users);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> CreateUser(UserModelDto model) {
        return CompletableFuture.completedFuture(
            userRepo.CreateUser(UserModelMapper.asEntity(model))
            );
    }

    @Async
    @Override
    public CompletableFuture<Boolean> updateUser(UserModelDto model) {
        return CompletableFuture.completedFuture(
            userRepo.updateUser(UserModelMapper.asEntity(model)));
    }

    @Async
    @Override
    @Scheduled(fixedDelay = 1000*60) // увеличение балланса раз в минуту
    public CompletableFuture<Boolean> balanceIncreaceByMinute() 
    {
        return CompletableFuture.completedFuture(userRepo.balanceIncreaceByMinute());
    }

    @Async
    @Override
    public CompletableFuture<UserModelDto> getUserById(UUID userId) {
        return CompletableFuture.completedFuture(
            UserModelMapper.asDto(userRepo.getUserById(userId))
        );
    }

    @Async
    @Override
    public CompletableFuture<List<UserModelDto>> getUserByName(String fio) {
        List<UserModelDto> users = userRepo.getUserByName(fio)
                                    .stream()
                                    .map(UserModelMapper::asDto)
                                    .collect(Collectors.toList());

        return CompletableFuture.completedFuture(users);
    }

    @Async
    @Override
    public CompletableFuture<UserModelDto> getUserByPhone(UserPhoneDto phone) {
        return CompletableFuture.completedFuture(
            UserModelMapper.asDto(userRepo.getUserByPhone(UserPhoneMapper.asEntity(phone)))
        );
    }

    @Async
    @Override
    public CompletableFuture<UserModelDto> getUserByEmail(UserEmailDto email) {
        return CompletableFuture.completedFuture(
            UserModelMapper.asDto(userRepo.getUserByEmail(UserEmailMapper.asEntity(email)))
        );
    }

    @Async
    @Override
    public CompletableFuture<List<UserModelDto>> getUsersByBirthDate(Date birthDate) {
        List<UserModelDto> users = userRepo.getUsersByBirthDate(birthDate)
                                    .stream()
                                    .map(UserModelMapper::asDto)
                                    .collect(Collectors.toList());

        return CompletableFuture.completedFuture(users);
    }


    
}
