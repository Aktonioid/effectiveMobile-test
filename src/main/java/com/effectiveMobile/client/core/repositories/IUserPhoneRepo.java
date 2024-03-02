package com.effectiveMobile.client.core.repositories;

import java.util.List;
import java.util.UUID;

import com.effectiveMobile.client.core.models.UserPhone;

public interface IUserPhoneRepo 
{
    public List<UserPhone> getAllPhones(); // для тестов
    public List<UserPhone> getPhonesByUserId(UUID userId);
    public Long countPhonesByUserId(UUID userId);
    public UserPhone getPhoneByPhone(String phone);
    public boolean createUserPhone(UserPhone phone);
    public boolean delteUserPhone(UUID phoneId);
}
