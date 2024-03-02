package com.effectiveMobile.client.core.models;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refreshTokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenModel 
{
    @Id
    private UUID id; // id записи(токена) гененрируется при создании пары 
                    // access - refresh и нужна для того чтобы получать новую пару только по одному refresh токену
    
    @Column(name = "expired_date")
    @Temporal(TemporalType.DATE)
    @Basic
    private Date expiredDate; // дата когда токен истекает

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    private String token;    
}
