package com.effectiveMobile.client.infrastructure.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.effectiveMobile.client.core.dtos.UserModelDto;
import com.effectiveMobile.client.core.mappers.UserModelMapper;
import com.effectiveMobile.client.core.models.UserModel;
import com.effectiveMobile.client.core.repositories.ITransactionRepo;
import com.effectiveMobile.client.core.service.ITransactionService;

@Service
@EnableAsync
public class TransactionService implements ITransactionService{

    @Autowired
    ITransactionRepo transactionRepo;

    @Async
    @Override
    public CompletableFuture<UserModelDto> moneyTransfer(UUID senderId, UUID recipientId, double transferSum) {
        UserModel model = transactionRepo.moneyTransfer(senderId, recipientId, transferSum);

        if(model == null)
        {
            return null;
        }

        return CompletableFuture.completedFuture(
            UserModelMapper.asDto(model)
            );
    }

    @Async
    @Override
    public CompletableFuture<Boolean> isEnoughMoney(UUID senderId, double transferSum) {
        return CompletableFuture.completedFuture(transactionRepo.isEnoughMoney(senderId, transferSum));
    }
    
}
