package com.effectiveMobile.client.configs;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.effectiveMobile.client.core.models.UserModel;
import com.effectiveMobile.client.core.repositories.IUserRepo;
import com.effectiveMobile.client.core.service.IJwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    
    @Autowired
    private IJwtService jwtService; 
    @Autowired
    private IUserRepo userRepo;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain)
                throws ServletException, IOException 
    {
        String authHeader = request.getHeader(HEADER_NAME);
        if(StringUtils.isEmpty(authHeader) || !authHeader.startsWith(BEARER_PREFIX))
        {
            filterChain.doFilter(request, response);
            return;
        }

        

        // System.out.println(authHeader);
        String jwt = authHeader.substring(BEARER_PREFIX.length());
        UUID id = null;

        try
        {
            id = UUID.fromString(jwtService.ExtractId(jwt));
        }
        catch(ExpiredJwtException e)
        {
            // response.sendError(403, "token expired");
            filterChain.doFilter(request, response);
            return;
        }
        catch(SignatureException e)
        {
            // response.sendError(403, "token not valid");
            filterChain.doFilter(request, response);
            return;
        }
        
        // String id = jwtService.ExtractId(jwt);
        
        if(id != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserModel userModel = null;

                userModel = userRepo.getUserById(id);
           

            if(jwtService.IsTokenValid(jwt, userModel))
            {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken
                    (userModel,
                    null,
                    userModel.getAuthorities());
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.getContext().setAuthentication(authToken);;
            }
            
            filterChain.doFilter(request, response);
        }

    }

}
