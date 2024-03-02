package com.effectiveMobile.client.core.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationModel 
{
    private String fio;

    private Date birthDate;

    private String password;

    private double balance;
    
    // как я понял из тз, то чтобы раз в минуту баланс увеличивался на 5% надо,
    // чтобы баланс пользователя был бы меньше чем (изначальный(первый депозит) *2,07)
    // Это значение первого депозита пользователя 
    private double basicDeposit;

    private String phone;
    private String email;
}
