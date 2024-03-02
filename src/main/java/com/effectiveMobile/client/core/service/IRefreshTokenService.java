package com.effectiveMobile.client.core.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.effectiveMobile.client.core.dtos.RefreshTokenModelDto;

public interface IRefreshTokenService 
{
    public CompletableFuture<RefreshTokenModelDto> GetTokenById(UUID tokenId);
    public CompletableFuture<Boolean> CreateToken(RefreshTokenModelDto token);
    public CompletableFuture<Boolean> DeleteTokenById(UUID tokenId);
}
