package com.jsp.rise_together.service.Implements;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.jsp.rise_together.entity.Event;
import com.jsp.rise_together.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public String ngoEvent(Event event, BindingResult result) {
        eventRepository.save(event);
        return "/ngo/home";
    }

    public void save(Event event) {
        eventRepository.save(event);
    }

    public List<Event> findAll() {
    
       return eventRepository.findAll();
    }

    // public Event getEventById(Long id) {
    // return eventRepository.findById(id).orElse(null);
    // }

    public void update(Event event) {
       eventRepository.findById(event.getId());
    }

    public void deleteById(int id) {
        eventRepository.deleteById(id);
    }

    public long getUpcomingEventCount(){
       return eventRepository.countByDateGreaterThanEqual(LocalDate.now());

    }
    
    
}
