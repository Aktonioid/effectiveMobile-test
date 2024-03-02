package com.effectiveMobile.client.core.service;


import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.effectiveMobile.client.core.dtos.UserEmailDto;
import com.effectiveMobile.client.core.dtos.UserModelDto;
import com.effectiveMobile.client.core.dtos.UserPhoneDto;

public interface IUserService 
{
    public UserDetailsService loadUserDetailsById();

    //получение всех пользователей и создание новых только для тестов и быстроты создания, потом удалю
    public CompletableFuture<List<UserModelDto>> getAllUsers();
    public CompletableFuture<Boolean> CreateUser(UserModelDto model);

    public CompletableFuture<Boolean> updateUser(UserModelDto model);// тестовый update
    public CompletableFuture<Boolean> balanceIncreaceByMinute(); // возрастание баланса пользователей на 5% но не выше, чем 207%
    public CompletableFuture<UserModelDto> getUserById(UUID userId);
    public CompletableFuture<List<UserModelDto>> getUserByName(String fio);
    public CompletableFuture<UserModelDto> getUserByPhone(UserPhoneDto phone); 
    public CompletableFuture<UserModelDto> getUserByEmail(UserEmailDto email);
    public CompletableFuture<List<UserModelDto>> getUsersByBirthDate(Date birthDate);
    //проверка на то есть ли пользователь осуществляется путем получения пользователя из бд по id. Если null - пользователя нет

    // добавление и удаление номеров для пользователя будет проводиться в репо номеров и почты

}
