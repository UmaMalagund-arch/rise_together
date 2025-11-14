package com.jsp.rise_together.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private Long phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Transient // ✅ Do NOT store confirmPassword in DB
    private String confirmPassword;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
private boolean verified = false; // Default false until OTP verified

@Column(nullable = true)
private String profileImagePath;


}



