package com.jsp.rise_together.dto;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportPersondto {
    @NotNull(message = "Enter your mobile number")
    private Long mobile;
    private String name;
    @NotNull(message = "it is required field")
    private Integer age;
    @NotEmpty(message = "please choose category")
    private String category;
    @NotEmpty(message = "it is required")
    private String location;

    @NotNull(message = "Please upload image")
    private MultipartFile image;

    @NotNull(message = "Please select near by NGO")
    private Long ngoId;

}
