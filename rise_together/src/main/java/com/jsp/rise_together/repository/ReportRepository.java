package com.jsp.rise_together.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.rise_together.entity.NgoRegister;
import com.jsp.rise_together.entity.ReportedPerson;
import java.util.List;


public interface ReportRepository extends JpaRepository<ReportedPerson,Integer> {
 
    List<ReportedPerson> findByNgoId(NgoRegister ngo);
}
