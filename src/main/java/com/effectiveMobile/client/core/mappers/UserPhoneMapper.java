package com.effectiveMobile.client.core.mappers;

import com.effectiveMobile.client.core.dtos.UserPhoneDto;
import com.effectiveMobile.client.core.models.UserModel;
import com.effectiveMobile.client.core.models.UserPhone;

public class UserPhoneMapper 
{
    public static UserPhone asEntity(UserPhoneDto userPhoneDto)
    {
        return new UserPhone(
            userPhoneDto.getId(),
            new UserModel(),
            userPhoneDto.getPhoneNumber()
        );
    }
    public static UserPhoneDto asDto(UserPhone userPhone)
    {
        return new UserPhoneDto(
            userPhone.getId(),
            userPhone.getUser().getId(),
            userPhone.getPhoneNumber() 
        );
    }
}
