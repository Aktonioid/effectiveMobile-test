package com.effectiveMobile.client.infrastructure.controllers;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.effectiveMobile.client.core.dtos.LogInModel;
import com.effectiveMobile.client.core.dtos.RefreshTokenModelDto;
import com.effectiveMobile.client.core.dtos.RegistrationModel;
import com.effectiveMobile.client.core.dtos.UserEmailDto;
import com.effectiveMobile.client.core.dtos.UserModelDto;
import com.effectiveMobile.client.core.dtos.UserPhoneDto;
import com.effectiveMobile.client.core.mappers.UserModelMapper;
import com.effectiveMobile.client.core.service.IAuthenticationService;
import com.effectiveMobile.client.core.service.IJwtService;
import com.effectiveMobile.client.core.service.IRefreshTokenService;
import com.effectiveMobile.client.core.service.IUserEmailService;
import com.effectiveMobile.client.core.service.IUserService;
import com.effectiveMobile.client.infrastructure.services.JwtService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController 
{
    @Autowired
    IAuthenticationService authenticationService;
    @Autowired 
    IRefreshTokenService refreshTokenService;
    @Autowired
    IUserService userService;
    @Autowired
    IJwtService jwtService;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    IUserEmailService userEmailService;
    
    @PostMapping("/register")
    public ResponseEntity<String> UserRegistration(@RequestBody RegistrationModel register, HttpServletResponse response) throws InterruptedException, ExecutionException // мб надо прописать отдельный класс для ответов, так как токен одни хрен в куках пищется
, MessagingException
    {
        if(register == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // указывет на то указаны ли при регистрации уже существующие в бд email и телефон
        String mistakes = "";
        
        try
        {
            mistakes = authenticationService.SignUp(null).get();
        } 
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!mistakes.isEmpty())
        {
            return new ResponseEntity<String>(mistakes.toString(), HttpStatus.BAD_REQUEST);
        }

        UUID userId = UUID.randomUUID();

        UserModelDto userModel = new UserModelDto();
        UserEmailDto emailDto = new UserEmailDto();
        emailDto.setUserId(userId);
        emailDto.setEmail(register.getEmail());
        UserPhoneDto phoneDto = new UserPhoneDto();
        phoneDto.setUserId(userId);
        phoneDto.setPhoneNumber(register.getPhone());

        userModel.setId(userId);
        
        userService.CreateUser(userModel);//создаем модель пользователя

        UUID tokenId = UUID.randomUUID();

        String accessToken =jwtService.GenerateAccessToken(UserModelMapper.asEntity(userModel), tokenId);
        String refreshToken = jwtService.GenerateRefreshToken(UserModelMapper.asEntity(userModel), tokenId);

        refreshTokenService.CreateToken(new RefreshTokenModelDto(tokenId, refreshToken,
                                     new Date(System.currentTimeMillis() +JwtService.refreshTokenExpitaion))).get();

        Cookie refreshCookie = new Cookie("refresh", refreshToken);
        refreshCookie.setHttpOnly(true);

        Cookie accessCookie = new Cookie("access", accessToken);
        accessCookie.setHttpOnly(true);

        response.addCookie(refreshCookie);
        response.addCookie(accessCookie);

        //потом просто пустоту в ответ давать будет, а токен он возвращает чтоб мне удобней было проверять
        return ResponseEntity.ok(accessToken+"\n\n "+userId.toString());
    }

    // вход в аккаунт по почте
    @PostMapping("/login")
    public ResponseEntity<String> LogInByEmail(@RequestBody LogInModel model, HttpServletResponse response) throws InterruptedException, ExecutionException
    {
        model.setPassword(encoder.encode(model.getPassword()));
        if(!authenticationService.SingInByEmail(model).get())
        {
            return ResponseEntity.badRequest().body("Email или пароль не верны");
        }
       
        UserEmailDto email = userEmailService.getEmailByEmail(model.getEmail()).get();

        UUID tokenId = UUID.randomUUID();
        UserModelDto dto = userService.getUserByEmail(email).get();
        
        String accessToken = jwtService.GenerateAccessToken(UserModelMapper.asEntity(dto),tokenId);
        String refreshToken = jwtService.GenerateRefreshToken(UserModelMapper.asEntity(dto), tokenId);

        refreshTokenService.CreateToken(new RefreshTokenModelDto(tokenId, refreshToken,
                                     new Date(System.currentTimeMillis() +JwtService.refreshTokenExpitaion))).get();

        Cookie refreshCookie = new Cookie("refresh", refreshToken);
        refreshCookie.setHttpOnly(true);

        Cookie accessCookie = new Cookie("access", accessToken);
        accessCookie.setHttpOnly(true);

        response.addCookie(refreshCookie);
        response.addCookie(accessCookie);


        return ResponseEntity.ok(accessToken);
    }

    // получение новой пары access и refresh токена
    @PostMapping("/refresh")
    public ResponseEntity<String> RefreshAccessToken(String refreshToken ,
                                                    @RequestHeader("Authorization")String token, 
                                                    HttpServletResponse response) throws InterruptedException, ExecutionException
    {   
        
        String accessToken = token.split("Bearer ")[1];
        
        // проверка на валидность access token
        if(!jwtService.IsTokenValidNoTime(accessToken))
        {
            Cookie refreshCookie = new Cookie("refresh", null);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setMaxAge(0);

            Cookie accessCookie = new Cookie("access", null);
            accessCookie.setHttpOnly(true);
            accessCookie.setMaxAge(0);

            System.out.println("token invalid");

            response.addCookie(refreshCookie);
            response.addCookie(accessCookie);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UUID tokenId = UUID.fromString(jwtService.ExtractTokenId(accessToken));

        RefreshTokenModelDto refreshDto = refreshTokenService.GetTokenById(tokenId).get();
        
        // если в бд нет токена по такому id тогда удаляем куки
        if(refreshToken == null)
        {
            System.out.println("refresh is null");
            Cookie refreshCookie = new Cookie("refresh", null);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setMaxAge(0);

            Cookie accessCookie = new Cookie("access", null);
            accessCookie.setHttpOnly(true);
            accessCookie.setMaxAge(0);

            response.addCookie(refreshCookie);
            response.addCookie(accessCookie);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Если токен полученный из бд неравен токену из запроса тогда удаляем куки
        if(!refreshDto.getToken().equals(refreshToken))
        {
            System.out.println("refreshes not equals");
            Cookie refreshCookie = new Cookie("refresh", null);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setMaxAge(0);

            Cookie accessCookie = new Cookie("access", null);
            accessCookie.setHttpOnly(true);
            accessCookie.setMaxAge(0);

            response.addCookie(refreshCookie);
            response.addCookie(accessCookie);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // если токен истек
        if(refreshDto.getExpiredDate().before(new Date(System.currentTimeMillis())))
        {
            System.out.println("refresh expired");
            Cookie refreshCookie = new Cookie("refresh", null);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setMaxAge(0);

            Cookie accessCookie = new Cookie("access", null);
            accessCookie.setHttpOnly(true);
            accessCookie.setMaxAge(0);

            response.addCookie(refreshCookie);
            response.addCookie(accessCookie);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserModelDto dto = userService.getUserById(UUID.fromString(jwtService.ExtractId(accessToken))).get();

        refreshTokenService.DeleteTokenById(tokenId).get();

        tokenId = UUID.randomUUID();

        accessToken = jwtService.GenerateAccessToken(UserModelMapper.asEntity(dto), tokenId);
        refreshToken = jwtService.GenerateRefreshToken(UserModelMapper.asEntity(dto), tokenId);

        refreshTokenService.CreateToken(new RefreshTokenModelDto(tokenId, refreshToken, 
                                        new Date(System.currentTimeMillis() +JwtService.refreshTokenExpitaion))).get();

        Cookie refreshCookie = new Cookie("refresh", refreshToken);
        refreshCookie.setHttpOnly(true);

        Cookie accessCookie = new Cookie("access", accessToken);
        accessCookie.setHttpOnly(true);

        response.addCookie(refreshCookie);
        response.addCookie(accessCookie);

        return ResponseEntity.ok(accessToken);
    }

    // выход из аккаунта
    @PostMapping("/logout")
    public ResponseEntity<String> LogOut(String accessToken, HttpServletResponse response)
    {
        // получение uuid токена
        UUID tokenId = UUID.fromString(jwtService.ExtractTokenId(accessToken));
        
        refreshTokenService.DeleteTokenById(tokenId);

        Cookie refreshCookie = new Cookie("refresh", "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(0);

        Cookie accessCookie = new Cookie("access", "");
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(0);

        response.addCookie(refreshCookie);
        response.addCookie(accessCookie);

        return ResponseEntity.ok("logged out");
    }    
}
