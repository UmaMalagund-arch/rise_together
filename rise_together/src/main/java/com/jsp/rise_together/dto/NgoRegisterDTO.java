package com.jsp.rise_together.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NgoRegisterDTO {
   @NotEmpty(message = "It is required Field")
    private String name;
    @Size(min=10,max = 1000,message = "Enter between 10-1000 characters")
    private String description;
    @NotEmpty(message = "It is required Field")
    private String address;
    @NotNull(message = "It is required Field")
    private Long pinCode;
    @DecimalMin(value = "6000000000",message = "Enter Proper Mobile Number")
    @DecimalMax(value = "9999999999",message = "Enter the proper Mobile number")
    private Long telephoneNumber;
    @Email(message = "Enter proper Email")
    @NotEmpty(message = "It is Require Field")
    private String email;
    @Size(min=3,max=30, message = "Enter between 3-30 characters")
    private String personName;
    @DecimalMin(value = "6000000000",message = "Enter Proper Mobile Number")
    @DecimalMax(value = "9999999999",message = "")
    private Long personMobile;
    @NotEmpty(message = "It is Required Field")
    private String registrationType;
    @NotEmpty(message = "It is Required Filed")
    private String registerNumber;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "It is Required Field")
    private LocalDate registerDate;
    private MultipartFile logo;
    @NotNull(message = "Please upload registration proof")
    private MultipartFile registrationProof;
     @AssertTrue(message = "You must accept the terms and conditions")
    private Boolean terms;
}
