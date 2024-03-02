package com.effectiveMobile.client.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_model")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModel implements UserDetails
{
    @Id
    private UUID id;

    @Column
    private String username;//Используем как ФИО пользователя

    @Temporal(value = TemporalType.DATE)
    @Basic
    @Column(name = "birth_date")
    private Date birthDate;

    @Column
    private String password;

    @Column
    private double balance;
    
    // как я понял из тз, то чтобы раз в минуту баланс увеличивался на 5% надо,
    // чтобы баланс пользователя был бы меньше чем (изначальный(первый депозит) *2,07)
    // Это значение первого депозита пользователя 
    @Column(name = "basic_deposit")
    private double basicDeposit;

    @JoinColumn
    @OneToMany(fetch = FetchType.EAGER)
    private Set<UserEmail> emails;

    @JoinColumn(name = "user_id")
    @OneToMany(fetch = FetchType.EAGER)
    private Set<UserPhone> phones;

    public UserModel(UUID id)
    {
        this.id = id;
    }

    @Override
    public boolean isAccountNonExpired() 
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() 
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() 
    {
        return true;
    }

    @Override
    public boolean isEnabled() 
    {
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() 
    {
        return new ArrayList<>();
    }
}
