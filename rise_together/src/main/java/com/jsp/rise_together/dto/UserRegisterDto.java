package com.jsp.rise_together.dto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Name should be 3-20 characters")
    private String username;

    @NotNull(message = "Phone no. is required")
    @DecimalMin(value ="6000000000", message ="Enter Proper Mobile Number")
    @DecimalMax(value ="9999999999", message ="Enter Proper Mobile Number")
    private Long phone;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",message = "Invalid email format")
    private String email;

    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
        message = "Password must be 8-20 characters long, include at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;

    
    private String confirmPassword;

    @NotBlank(message = "Address is required")
    private String address;
}




