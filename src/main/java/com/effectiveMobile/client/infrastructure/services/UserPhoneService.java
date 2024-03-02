package com.effectiveMobile.client.infrastructure.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.effectiveMobile.client.core.dtos.UserPhoneDto;
import com.effectiveMobile.client.core.mappers.UserPhoneMapper;
import com.effectiveMobile.client.core.repositories.IUserPhoneRepo;
import com.effectiveMobile.client.core.service.IUserPhoneService;

@Service
@EnableAsync(proxyTargetClass = true)
@EnableCaching(proxyTargetClass=true)
public class UserPhoneService implements IUserPhoneService{

    @Autowired
    IUserPhoneRepo userPhoneRepo;

    @Async
    @Override
    public CompletableFuture<List<UserPhoneDto>> getAllPhones() {
        List<UserPhoneDto> phones = userPhoneRepo.getAllPhones().stream()
                                    .map(UserPhoneMapper::asDto)
                                    .collect(Collectors.toList());
        
        return CompletableFuture.completedFuture(phones);
    }

    @Async
    @Override
    public CompletableFuture<List<UserPhoneDto>> getPhonesByUserId(UUID userId) {
        List<UserPhoneDto> phones = userPhoneRepo.getPhonesByUserId(userId)
                                    .stream()
                                    .map(UserPhoneMapper::asDto)
                                    .collect(Collectors.toList());
    
        return CompletableFuture.completedFuture(phones);
    }

    @Async
    @Override
    public CompletableFuture<Long> countPhonesByUserId(UUID userId) {

        return CompletableFuture.completedFuture(userPhoneRepo.countPhonesByUserId(userId));
    }

    @Async
    @Override
    public CompletableFuture<UserPhoneDto> getPhoneByPhone(String phone) {
        return CompletableFuture.completedFuture(
                    UserPhoneMapper.asDto(userPhoneRepo.getPhoneByPhone(phone)));
    }

    @Async
    @Override
    public CompletableFuture<Boolean> createUserPhone(UserPhoneDto phone) {
        return CompletableFuture.completedFuture(userPhoneRepo.createUserPhone(
            UserPhoneMapper.asEntity(phone)
            ));
    }

    @Async
    @Override
    public CompletableFuture<Boolean> delteUserPhone(UUID phoneId) {
        return CompletableFuture.completedFuture(userPhoneRepo.delteUserPhone(phoneId));
    }
    
}
