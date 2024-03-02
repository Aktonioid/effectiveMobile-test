package com.effectiveMobile.client.infrastructure.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.effectiveMobile.client.core.dtos.RefreshTokenModelDto;
import com.effectiveMobile.client.core.mappers.RefreshTokenMapper;
import com.effectiveMobile.client.core.models.RefreshTokenModel;
import com.effectiveMobile.client.core.repositories.IRefreshTokenRepo;
import com.effectiveMobile.client.core.service.IRefreshTokenService;

@Service
@EnableAsync
public class RefreshTokenService implements IRefreshTokenService
{
    @Autowired
    IRefreshTokenRepo refreshTokenRepo;

    @Override
    @Async
    public CompletableFuture<RefreshTokenModelDto> GetTokenById(UUID tokenId) 
    {
        RefreshTokenModel model = refreshTokenRepo.GetTokenById(tokenId);

        if(model == null)
        {
            return null;
        }
        return CompletableFuture.completedFuture(RefreshTokenMapper.asDto(model));
    }

    @Override
    @Async
    public CompletableFuture<Boolean> CreateToken(RefreshTokenModelDto token) 
    {
        System.out.println("что-то");
        return CompletableFuture.completedFuture(refreshTokenRepo.CreateToken(RefreshTokenMapper.asEntity(token)));
    }

    @Override
    @Async
    public CompletableFuture<Boolean> DeleteTokenById(UUID tokenId) 
    {
        return CompletableFuture.completedFuture(refreshTokenRepo.DeleteTokenById(tokenId));
    }

    
}
