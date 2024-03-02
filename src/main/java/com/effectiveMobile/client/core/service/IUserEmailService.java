package com.effectiveMobile.client.core.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.effectiveMobile.client.core.dtos.UserEmailDto;

public interface IUserEmailService 
{
    public CompletableFuture<List<UserEmailDto>> getAllEmais(); //для тестов
    public CompletableFuture<List<UserEmailDto>> getEmailsByUser(UUID userId);
    public CompletableFuture<UserEmailDto> getEmailByEmail(String email);
    public CompletableFuture<Long> coutnEmailsByUserId(UUID userId);// подсчет сколько почт указано у пользователя
    public CompletableFuture<Boolean> createEmail(UserEmailDto emal);
    public CompletableFuture<Boolean> deleteEmail(UUID id);
}
