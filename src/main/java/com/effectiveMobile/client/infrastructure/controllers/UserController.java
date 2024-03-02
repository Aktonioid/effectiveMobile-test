package com.effectiveMobile.client.infrastructure.controllers;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.effectiveMobile.client.core.dtos.UserEmailDto;
import com.effectiveMobile.client.core.dtos.UserModelDto;
import com.effectiveMobile.client.core.dtos.UserPhoneDto;
import com.effectiveMobile.client.core.service.IUserEmailService;
import com.effectiveMobile.client.core.service.IUserPhoneService;
import com.effectiveMobile.client.core.service.IUserService;

@RestController
@RequestMapping("/users")
public class UserController 
{
    @Autowired
    IUserService userService;
    @Autowired
    IUserEmailService userEmailService;
    @Autowired
    IUserPhoneService userPhoneService;

    @GetMapping("/byname")
    public ResponseEntity<List<UserModelDto>> getUsersByName(String fio) throws InterruptedException, ExecutionException
    {
        return ResponseEntity.ok(userService.getUserByName(fio).get());
    }

    @GetMapping("/bybirth")
    public ResponseEntity<List<UserModelDto>> getUsersByBirthDate(Date birthDate) throws InterruptedException, ExecutionException
    {
        return ResponseEntity.ok(userService.getUsersByBirthDate(birthDate).get());
    }

    @GetMapping("/byemail")
    public ResponseEntity<UserModelDto> getUserByEmail(String email) throws InterruptedException, ExecutionException
    {
        UserEmailDto userEmail = userEmailService.getEmailByEmail(email).get();

        if(userEmail == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(userService.getUserByEmail(userEmail).get());
    }

    @GetMapping("/byphone")
    public ResponseEntity<UserModelDto> getUserByPhone(String phone) throws InterruptedException, ExecutionException
    {
        UserPhoneDto userPhone = userPhoneService.getPhoneByPhone(phone).get();

        if(userPhone == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(userService.getUserByPhone(userPhone).get());
    }

}
