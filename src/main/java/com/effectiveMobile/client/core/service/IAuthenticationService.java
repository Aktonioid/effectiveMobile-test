package com.effectiveMobile.client.core.service;

import java.util.concurrent.CompletableFuture;

import com.effectiveMobile.client.core.dtos.LogInModel;
import com.effectiveMobile.client.core.dtos.RegistrationModel;


public interface IAuthenticationService 
{
    public CompletableFuture<String> SignUp(RegistrationModel model); // регистрация
    public CompletableFuture<Boolean> SingInByEmail(LogInModel login);
}
