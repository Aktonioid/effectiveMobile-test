package com.effectiveMobile.client.infrastructure.controllers;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.effectiveMobile.client.core.service.ITransactionService;
import com.effectiveMobile.client.core.service.IUserService;

@RestController
@RequestMapping("/transaction")
public class TransactionController 
{
    @Autowired
    ITransactionService transactionService;
    @Autowired
    IUserService userService;

    public ResponseEntity<String> sendMoney(UUID recipientId, UUID senderId, double transferSum) throws InterruptedException, ExecutionException
    {
        // проверка на то что денег хватает 
        if(!transactionService.isEnoughMoney(senderId, transferSum).get())
        {
            return ResponseEntity.badRequest().body("денег нет");
        }
        
        // проверка, что есть кому отправлять
        if(userService.getUserById(recipientId).get() == null)
        {
            return ResponseEntity.badRequest().body("unknown user");
        }

        // отправка денег
        if(transactionService.moneyTransfer(senderId, recipientId, transferSum).get() == null)
        {
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        }

        

        return ResponseEntity.ok("sended");
    } 
    
}
