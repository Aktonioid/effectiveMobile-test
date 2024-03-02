package com.effectiveMobile.client.core.service;

import java.util.UUID;

import com.effectiveMobile.client.core.models.UserModel;

public interface IJwtService 
{
    public String ExtractUserName(String token);
    public String ExtractEmail(String token);
    public String ExtractId(String token);    
    public String ExtractTokenId(String token);
    public String GenerateAccessToken(UserModel user, UUID tokenId);
    public String GenerateRefreshToken(UserModel user, UUID tokenId);
    public boolean IsTokenValid(String token, UserModel user);
    public boolean IsTokenValidNoTime(String token);    
}
