package com.effectiveMobile.client.core.mappers;

import com.effectiveMobile.client.core.dtos.UserEmailDto;
import com.effectiveMobile.client.core.models.UserEmail;
import com.effectiveMobile.client.core.models.UserModel;

public class UserEmailMapper 
{
    public static UserEmail asEntity(UserEmailDto dto)
    {
        return new UserEmail(
            dto.getId(),
            new UserModel(dto.getUserId()),
            dto.getEmail()
        );
    }
    public static UserEmailDto asDto(UserEmail model)
    {
        return new UserEmailDto(
            model.getId(),
            model.getUserModel().getId(),
            model.getEmail()
        );
    }
}
