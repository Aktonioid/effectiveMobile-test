package com.effectiveMobile.client.core.repositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.effectiveMobile.client.core.models.UserEmail;
import com.effectiveMobile.client.core.models.UserModel;
import com.effectiveMobile.client.core.models.UserPhone;

public interface IUserRepo 
{
    //получение всех пользователей и создание новых только для тестов и быстроты создания, потом удалю
    public List<UserModel> getAllUsers();
    public boolean CreateUser(UserModel model);

    public boolean updateUser(UserModel model);// тестовый update
    public boolean balanceIncreaceByMinute(); // возрастание баланса пользователей на 5% но не выше, чем 207%
    public UserModel getUserById(UUID userId);
    public List<UserModel> getUserByName(String fio);
    public UserModel getUserByPhone(UserPhone phone); 
    public UserModel getUserByEmail(UserEmail email);
    public List<UserModel> getUsersByBirthDate(Date birthDate);
       //проверка на то есть ли пользователь осуществляется путем получения пользователя из бд по id. Если null - пользователя нет

    // добавление и удаление номеров для пользователя будет проводиться в репо номеров и почты
    
}
