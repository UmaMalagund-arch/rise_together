package com.jsp.rise_together.repository;


import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;


import com.jsp.rise_together.entity.Event;


public interface EventRepository extends JpaRepository<Event,Integer> {
    

   long countByDateGreaterThanEqual(LocalDate date);
}
