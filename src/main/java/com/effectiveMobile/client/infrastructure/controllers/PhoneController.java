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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.effectiveMobile.client.core.dtos.UserPhoneDto;
import com.effectiveMobile.client.core.service.IUserPhoneService;


@RestController
@RequestMapping("/phones")
public class PhoneController {
    
    @Autowired
    IUserPhoneService userPhoneService;

    @GetMapping("/byuserid")
    public ResponseEntity<List<UserPhoneDto>> getPhonesbyUserId(UUID userId) throws InterruptedException, ExecutionException
    {
        return ResponseEntity.ok(userPhoneService.getPhonesByUserId(userId).get());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPhone(@RequestBody UserPhoneDto phone) throws InterruptedException, ExecutionException
    {

        //проверка на то есть ли такой телефон в бд
        if(userPhoneService.getPhoneByPhone(phone.getPhoneNumber()).get() != null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!userPhoneService.createUserPhone(phone).get())
        {
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        }
        
        return ResponseEntity.ok("ok");
    }
    
    @DeleteMapping("/")
    public ResponseEntity<String> deletePhone(UUID userId, UUID phoneId) throws InterruptedException, ExecutionException
    {
        // проверка на то последний ли телефон у пользоватля
        if(userPhoneService.countPhonesByUserId(userId).get() < 2)
        {
            return ResponseEntity.badRequest().body("last phone");
        }

        if(!userPhoneService.delteUserPhone(phoneId).get())
        {
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        }

        return ResponseEntity.ok("deleted");
    }
}
