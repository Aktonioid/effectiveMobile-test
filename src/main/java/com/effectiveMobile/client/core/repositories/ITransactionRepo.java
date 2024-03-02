package com.effectiveMobile.client.core.repositories;

import java.util.List;
import java.util.UUID;

import com.effectiveMobile.client.core.models.UserModel;

public interface ITransactionRepo 
{
    //получаем только отправителя
    public UserModel moneyTransfer(UUID senderId, UUID recipientId, double transferSum);// отправка денег
    public boolean isEnoughMoney(UUID senderId, double transferSum); // проверка на то хватает ли денег у отправителя

}
