package com.effectiveMobile.client.core.dtos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModelDto 
{
    private UUID id;
    private String fio;
    private Date birthDate;
    private String password;
    private double balance;
    private double basicDeposit;
    private List<UserEmailDto> emails;
    private List<UserPhoneDto> phones;
}
