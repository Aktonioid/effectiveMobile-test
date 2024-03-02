package com.effectiveMobile.client.infrastructure.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.effectiveMobile.client.core.dtos.LogInModel;
import com.effectiveMobile.client.core.dtos.RegistrationModel;
import com.effectiveMobile.client.core.models.UserEmail;
import com.effectiveMobile.client.core.models.UserModel;
import com.effectiveMobile.client.core.repositories.IUserEmailRepo;
import com.effectiveMobile.client.core.repositories.IUserPhoneRepo;
import com.effectiveMobile.client.core.repositories.IUserRepo;
import com.effectiveMobile.client.core.service.IAuthenticationService;

@Service
@EnableAsync
public class AuthenticationService implements IAuthenticationService{
    @Autowired
    IUserRepo userRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    IUserEmailRepo emailRepo;
    @Autowired
    IUserPhoneRepo phoneRepo;

    //выдает какие именно поля уже есть в бд
    @Async
    @Override
    public CompletableFuture<String> SignUp(RegistrationModel model) 
    {
        StringBuffer sb = new StringBuffer();

        if(phoneRepo.getPhoneByPhone(model.getPhone()) != null)
        {
            sb.append("phone ");
        }
    
        if(emailRepo.getEmailByEmail(model.getEmail()) != null)
        {
            sb.append("email");
        }

        if(!sb.toString().isEmpty())
        {
            return CompletableFuture.completedFuture(sb.toString());
        }

        return CompletableFuture.completedFuture("");
    }
    
    @Async
    @Override
    public CompletableFuture<Boolean> SingInByEmail(LogInModel login) 
    {
        UserEmail email = emailRepo.getEmailByEmail(login.getEmail());

        //Если есть email, значит и user тоже
        if(email== null)
        {
            return CompletableFuture.completedFuture(false);
        }
        
        UserModel user = userRepo.getUserByEmail(email);
        if(!encoder.matches(login.getPassword(), user.getPassword()))
        {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.completedFuture(true); // Все ок
    }
}
