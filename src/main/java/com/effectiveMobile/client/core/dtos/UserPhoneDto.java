package com.effectiveMobile.client.core.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneDto 
{
    private UUID id;
    private UUID userId;
    private String phoneNumber;
}
