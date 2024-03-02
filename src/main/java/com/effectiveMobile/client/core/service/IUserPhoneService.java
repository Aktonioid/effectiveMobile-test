package com.effectiveMobile.client.core.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import com.effectiveMobile.client.core.dtos.UserPhoneDto;

@EnableAsync(proxyTargetClass = true)
@EnableCaching(proxyTargetClass=true)
public interface IUserPhoneService 
{
    public CompletableFuture<List<UserPhoneDto>> getAllPhones(); // для тестов
    public CompletableFuture<List<UserPhoneDto>> getPhonesByUserId(UUID userId);
    public CompletableFuture<Long> countPhonesByUserId(UUID userId);
    public CompletableFuture<UserPhoneDto> getPhoneByPhone(String phone);
    public CompletableFuture<Boolean> createUserPhone(UserPhoneDto phone);
    public CompletableFuture<Boolean> delteUserPhone(UUID phoneId);
}
