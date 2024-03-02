package com.effectiveMobile.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import com.effectiveMobile.client.core.dtos.UserModelDto;
import com.effectiveMobile.client.infrastructure.controllers.TransactionController;
import com.effectiveMobile.client.infrastructure.services.TransactionService;
import com.effectiveMobile.client.infrastructure.services.UserService;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionTests 
{

   
    @Mock
    UserService service = Mockito.mock(UserService.class);
    
    @Mock
    TransactionService transactionService = mock(TransactionService.class);
    @InjectMocks
    TransactionController controller;

    @Test
    void transactionRepoTransactionTest()
    {

        List<UserModelDto> users = new ArrayList<>();
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        
        UserModelDto sender = new UserModelDto(
            senderId,
            "sender",
            new Date(1015048469),
            "password",
            1500,
            1500,
            null,
            null);

        UserModelDto recipient = new UserModelDto(
            recipientId,
            "recepient",
            new Date(1015048469),
            "password",
            1500,
            1500,
            null,
            null);

        when(service.getAllUsers()).thenReturn(CompletableFuture.completedFuture(List.of(sender, recipient)));
        when(service.getUserById(senderId)).thenReturn(CompletableFuture.completedFuture(sender));
        when(service.getUserById(recipientId)).thenReturn(CompletableFuture.completedFuture(recipient));

        double transactionSum = 1200;

        when(transactionService.moneyTransfer(senderId, recipientId, transactionSum))
        .thenAnswer(model -> {
            UserModelDto modelDto = new UserModelDto(
                senderId,
                sender.getFio(),
                sender.getBirthDate(),
                sender.getPassword(),
                sender.getBalance() - transactionSum,
                sender.getBasicDeposit(),
                sender.getEmails(),
                sender.getPhones()
            );

            sender.setBalance(sender.getBalance() - transactionSum);
            recipient.setBalance(recipient.getBalance() + transactionSum);
            return CompletableFuture.completedFuture(modelDto);});

            when(transactionService.isEnoughMoney(senderId, transactionSum)).thenAnswer(answ ->
            {
                if(sender.getBalance() < transactionSum)
                {
                    return CompletableFuture.completedFuture(false);
                }
                return CompletableFuture.completedFuture(true);
            });

            ResponseEntity<String> response = null;

            try {
                response = controller.sendMoney(recipientId, senderId, transactionSum);
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            assertEquals(response, ResponseEntity.ok("sended"));
            assertEquals(recipient.getBalance(), 2700.0);
            assertEquals(sender.getBalance(), 300.0);
            
            try {
                response = controller.sendMoney(recipientId, senderId, transactionSum);
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // assertEquals(response, ResponseEntity.ok("sended"));

    }   
}
