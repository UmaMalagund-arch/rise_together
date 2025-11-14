package com.jsp.rise_together.entity;

import java.time.LocalDate;

import com.jsp.rise_together.dto.NgoRegisterDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class NgoRegister {
   @Id
   @GeneratedValue(strategy =GenerationType.IDENTITY)
   private Long id;
   @Column(nullable = false)
   private String name;
   @Column(nullable = true)
   private String description;
   @Column(nullable = false)
   private String address;
   @Column(nullable = false)
   private Long pinCode;
   @Column(nullable = false, unique = true)
   private Long telePhoneNumber;
   @Column(nullable = false,unique = true)
   private String email;
   @Column(nullable = false)
   private String personName;
   @Column(nullable = false, unique = true)
   private Long personMobile;
   @Column(nullable = false)
   private String registrationType;
   @Column(nullable = false, unique = true)
   private String registerNumber;
   @Column(nullable = false)
   private LocalDate registerDate;
   @Column(name = "registration_proof_path", length = 255, nullable = false)
   private String  registrationProof;
   @Column(nullable = true)
   private boolean approved;

   public NgoRegister(NgoRegisterDTO ngoRegisterDTO){
    this.name=ngoRegisterDTO.getName();
    this.description=ngoRegisterDTO.getDescription();
    this.address=ngoRegisterDTO.getAddress();
    this.pinCode=ngoRegisterDTO.getPinCode();
    this.telePhoneNumber=ngoRegisterDTO.getTelephoneNumber();
    this.email=ngoRegisterDTO.getEmail();
    this.personName=ngoRegisterDTO.getPersonName();
    this.personMobile=ngoRegisterDTO.getPersonMobile();
    this.registrationType=ngoRegisterDTO.getRegistrationType();
    this.registerNumber=ngoRegisterDTO.getRegisterNumber();
    this.registerDate=ngoRegisterDTO.getRegisterDate();
   }
}

