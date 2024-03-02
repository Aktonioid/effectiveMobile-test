package com.effectiveMobile.client.core.repositories;

import java.util.List;
import java.util.UUID;

import com.effectiveMobile.client.core.models.UserEmail;

public interface IUserEmailRepo 
{
    public List<UserEmail> getAllEmais(); //для тестов
    public List<UserEmail> getEmailsByUser(UUID userId);
    public UserEmail getEmailByEmail(String email);
    public Long coutnEmailsByUserId(UUID userId);// подсчет сколько почт указано у пользователя
    public boolean createEmail(UserEmail emal);
    public boolean deleteEmail(UUID id);
}
