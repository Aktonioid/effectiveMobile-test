package com.effectiveMobile.client.infrastructure.services;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.effectiveMobile.client.core.models.UserModel;
import com.effectiveMobile.client.core.service.IJwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;

@PropertySource("security.properties")
@Service
public class JwtService implements IJwtService
{
    @Autowired
    Environment env;

    //вероятно сделаю так, чтоб если что просто в одном месте менять
    public static long refreshTokenExpitaion = 1000 * 60 * 60 *24 * 7; 

    @Override
    public String ExtractUserName(String token) 
    {
        return ExtractClaims(token, Claims::getSubject);
    }

    @Override
    public String ExtractEmail(String token) 
    {
        return ExtractAllClaims(token, env.getProperty("jwt.secret")).get("email").toString();
    }

    @Override
    public String ExtractId(String token)
    {
        return ExtractAllClaims(token, env.getProperty("jwt.secret")).get("id").toString();
    }

    @Override
    public String ExtractTokenId(String token) 
    {
        return ExtractAllClaims(token, env.getProperty("jwt.secret")).get("token_id").toString();
    }

    @Override
    public String GenerateAccessToken(UserModel user, UUID tokenId) 
    {
        Map<String, Object> claims = new HashMap<>(); // claims

        claims.put("id", user.getId());
        claims.put("token_id", tokenId);

        return GenerateToken(claims, 
                            user, 
                            new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24), // токен валиден сутки 
                            GetKey(env.getProperty("jwt.secret"))
                            );
    }

    @Override
    public String GenerateRefreshToken(UserModel user, UUID tokenId) 
    {
        Map<String, Object> claims = new HashMap<>(); // claims

        claims.put("username", user.getUsername());
        claims.put("emails", user.getEmails());
        claims.put("token_id", tokenId);

        return GenerateToken(claims,
                            user, 
                            new Date(System.currentTimeMillis() + refreshTokenExpitaion), //токен будет валиден неделю 
                            GetKey(env.getProperty("refresh.secret")));
    }

    @Override
    public boolean IsTokenValid(String token, UserModel user) 
    {
        final String username = ExtractUserName(token);

        return (username.equals(user.getUsername())) && !IsTokenExpired(token); 
    }
    

    @Override
    // проверка подписи
    public boolean IsTokenValidNoTime(String token) 
    {

        try
        {
           ExtractAllClaims(token, env.getProperty("jwt.secret"));
        }
        catch(SignatureException e)
        {
            return false;
        }
        catch(ExpiredJwtException e)
        {

        }

        return true;
    }



    private String GenerateToken(Map<String, Object> extraClaims, UserModel user,
                                Date expirationDate, SecretKey key)
    {
        return Jwts.builder().claims(extraClaims).subject(user.getId().toString())
                    .issuedAt(new Date())
                    .expiration(expirationDate)
                    .signWith(key, Jwts.SIG.HS512)
                    .compact();
    }
    


    private <T> T ExtractClaims(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = ExtractAllClaims(token, env.getProperty("jwt.secret"));
        return claimsResolver.apply(claims);
    }

    private Claims ExtractAllClaims(String string, String secret)
    {
        return Jwts.parser().verifyWith(GetKey(secret)).build().parseSignedClaims(string).getPayload();
    }

    
    private SecretKey GetKey(String secret)
    {
        return new SecretKeySpec(
            Base64.getDecoder().decode(secret), 
            "HmacSHA512");
    }

    private boolean IsTokenExpired(String token)
    {
        return ExtractExpiration(token).before(new Date());
    }

    private Date ExtractExpiration(String token)
    {
        return ExtractClaims(token, Claims::getExpiration);
    }

}
