package com.jsp.rise_together.entity;

import com.jsp.rise_together.dto.ReportPersondto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ReportedPerson {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
     private Long mobile;
    @Column(nullable = false)
    private String name;
     @Column(nullable = true)
    private Integer age;
     @Column(nullable = true)
    private String category;
     @Column(nullable = true)
    private String location;

     @Column(nullable = true ,length = 500)
    private String image;

    @Column(nullable = true)
   private String status="pending";

    @ManyToOne
    @JoinColumn(name = "ngo_id")
    private NgoRegister ngoId; 

    public ReportedPerson(ReportPersondto reportPersondto,NgoRegister ngo){
        this.mobile=reportPersondto.getMobile();
        this.name=reportPersondto.getName();
        this.age=reportPersondto.getAge();
        this.category=reportPersondto.getCategory();
        this.location=reportPersondto.getLocation();
       this.ngoId=ngo;
       this.status="pending";
        
    }

}
