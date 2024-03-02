package com.effectiveMobile.client.core.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.effectiveMobile.client.core.dtos.UserModelDto;

public interface ITransactionService 
{
    public CompletableFuture<UserModelDto> moneyTransfer(UUID senderId, UUID recipientId, double transferSum);// отправка денег
    public CompletableFuture<Boolean> isEnoughMoney(UUID senderId, double transferSum); // проверка на то хватает ли денег у отправителя
}
