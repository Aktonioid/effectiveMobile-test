package com.effectiveMobile.client.core.mappers;

import com.effectiveMobile.client.core.dtos.RefreshTokenModelDto;
import com.effectiveMobile.client.core.models.RefreshTokenModel;

public class RefreshTokenMapper 
{
    public static RefreshTokenModel asEntity(RefreshTokenModelDto dto)
    {
        if(dto == null)
        {
            return null;
        }
        
        return new RefreshTokenModel(dto.getId(), dto.getExpiredDate(), dto.getToken());
    }  

    public static RefreshTokenModelDto asDto(RefreshTokenModel model)
    {
        if(model == null)
        {
            return null;
        }

        return new RefreshTokenModelDto(model.getId(), model.getToken(), model.getExpiredDate());
    }
}
