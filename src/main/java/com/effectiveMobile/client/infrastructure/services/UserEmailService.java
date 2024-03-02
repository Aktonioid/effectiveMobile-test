package com.effectiveMobile.client.infrastructure.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.effectiveMobile.client.core.dtos.UserEmailDto;
import com.effectiveMobile.client.core.mappers.UserEmailMapper;
import com.effectiveMobile.client.core.repositories.IUserEmailRepo;
import com.effectiveMobile.client.core.service.IUserEmailService;

@Service
@EnableAsync
public class UserEmailService implements IUserEmailService{

    @Autowired
    IUserEmailRepo emailRepo;

    @Async
    @Override
    public CompletableFuture<List<UserEmailDto>> getAllEmais() {
        List<UserEmailDto> emails = emailRepo.getAllEmais()
                                    .stream()
                                    .map(UserEmailMapper::asDto)
                                    .collect(Collectors.toList());

        return CompletableFuture.completedFuture(emails);
    }

    @Async
    @Override
    public CompletableFuture<List<UserEmailDto>> getEmailsByUser(UUID userId) {
        List<UserEmailDto> emails = emailRepo.getEmailsByUser(userId)
                                    .stream()
                                    .map(UserEmailMapper::asDto)
                                    .collect(Collectors.toList());

        return CompletableFuture.completedFuture(emails);
    }

    @Async
    @Override
    public CompletableFuture<UserEmailDto> getEmailByEmail(String email) {
        return CompletableFuture.completedFuture(
            UserEmailMapper.asDto(emailRepo.getEmailByEmail(email))
        );
    }

    @Async
    @Override
    public CompletableFuture<Long> coutnEmailsByUserId(UUID userId) {
        return CompletableFuture.completedFuture(emailRepo.coutnEmailsByUserId(userId));
    }

    @Async
    @Override
    public CompletableFuture<Boolean> createEmail(UserEmailDto emal) {
        return CompletableFuture.completedFuture(emailRepo
            .createEmail(UserEmailMapper.asEntity(emal))
            );
    }

    @Async
    @Override
    public CompletableFuture<Boolean> deleteEmail(UUID id) {
        return CompletableFuture.completedFuture(emailRepo.deleteEmail(id));
    }

    
    
}
