package com.jsp.rise_together.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.rise_together.entity.NgoRegister;
public interface NGORegisterRepository extends JpaRepository<NgoRegister,Long>{

    boolean existsByEmail(String email);

    boolean existsByRegisterNumber(String registerNumber);

    boolean existsByName(String name);

    NgoRegister getByEmail(String email);

    NgoRegister findByEmail(String email);

    long count();

}
