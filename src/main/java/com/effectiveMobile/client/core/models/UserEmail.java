package com.effectiveMobile.client.core.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_email")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEmail 
{
    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn(name = "user_model")
    @ManyToOne
    @JsonIgnore
    private UserModel userModel;

    @Column(unique = true)
    private String email;
}
