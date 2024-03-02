package com.effectiveMobile.client.core.mappers;

import java.util.stream.Collectors;

import com.effectiveMobile.client.core.dtos.UserModelDto;
import com.effectiveMobile.client.core.models.UserModel;

public class UserModelMapper
{
    public static UserModel asEntity(UserModelDto dto)
    {
        return new UserModel(
            dto.getId(),
            dto.getFio(),
            dto.getBirthDate(),
            dto.getPassword(),
            dto.getBalance(),
            dto.getBasicDeposit(),
            dto.getEmails()
                .stream()
                .map(UserEmailMapper::asEntity)
                .collect(Collectors.toSet()),
            dto.getPhones()
                .stream()
                .map(UserPhoneMapper::asEntity)
                .collect(Collectors.toSet())
        );
    }

    public static UserModelDto asDto(UserModel model)
    {   
        return new UserModelDto(
            model.getId(),
            model.getUsername(),
            model.getBirthDate(),
            model.getPassword(),
            model.getBalance(),
            model.getBasicDeposit(),
            model.getEmails()
                .stream()
                .map(UserEmailMapper::asDto)
                .collect(Collectors.toList()),
            model.getPhones()
                .stream()
                .map(UserPhoneMapper::asDto)
                .collect(Collectors.toList())
        );
    }
}
