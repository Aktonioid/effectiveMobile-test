package com.effectiveMobile.client.core.repositories;

import java.util.UUID;

import com.effectiveMobile.client.core.models.RefreshTokenModel;

public interface IRefreshTokenRepo 
{
    public RefreshTokenModel GetTokenById(UUID tokenId); // получить токен из бд
    public boolean CreateToken(RefreshTokenModel token); // создание нового токена
    public boolean DeleteTokenById(UUID tokenId); // удаление токена из бд
}
