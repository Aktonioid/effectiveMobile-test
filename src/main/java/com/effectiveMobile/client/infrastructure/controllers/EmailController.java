package com.effectiveMobile.client.infrastructure.controllers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.effectiveMobile.client.core.dtos.UserEmailDto;
import com.effectiveMobile.client.core.service.IUserEmailService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public class EmailController {
    
    @Autowired
    IUserEmailService userEmailService;

    @GetMapping("/byuserid")
    public ResponseEntity<List<UserEmailDto>> getPhonesbyUserId(UUID userId) throws InterruptedException, ExecutionException
    {
        return ResponseEntity.ok(userEmailService.getEmailsByUser(userId).get());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPhone(@RequestBody UserEmailDto email) throws InterruptedException, ExecutionException
    {

        //проверка на то есть ли такой телефон в бд
        if(userEmailService.getEmailByEmail(email.getEmail()).get() != null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!userEmailService.createEmail(email).get())
        {
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        }
        
        return ResponseEntity.ok("ok");
    }
    
    @DeleteMapping("/")
    public ResponseEntity<String> deletePhone(UUID userId, UUID emailId) throws InterruptedException, ExecutionException
    {
        // проверка на то последняя ли почта у пользоватля
        if(userEmailService.coutnEmailsByUserId(userId).get() < 2)
        {
            return ResponseEntity.badRequest().body("last phone");
        }

        if(!userEmailService.deleteEmail(emailId).get())
        {
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        }

        return ResponseEntity.ok("deleted");
    }
}
