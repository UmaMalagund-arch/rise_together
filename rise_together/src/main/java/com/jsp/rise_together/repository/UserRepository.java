package com.jsp.rise_together.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.rise_together.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);

    User findByPhone(Long phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(Long phone);

    User findByUsername(String name);





}
