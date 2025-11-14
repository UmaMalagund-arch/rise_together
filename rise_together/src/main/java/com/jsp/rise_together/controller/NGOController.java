package com.jsp.rise_together.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsp.rise_together.dto.NgoRegisterDTO;
import com.jsp.rise_together.entity.Event;
import com.jsp.rise_together.entity.NgoRegister;
import com.jsp.rise_together.entity.ReportedPerson;
import com.jsp.rise_together.repository.NGORegisterRepository;
import com.jsp.rise_together.repository.ReportRepository;
import com.jsp.rise_together.repository.UserRepository;
import com.jsp.rise_together.service.Implements.EventService;
import com.jsp.rise_together.service.Implements.NgoService;

import jakarta.servlet.http.HttpSession;





@Controller
@RequestMapping("/ngo/home")
public class NGOController {

    @Autowired
    EventService eventService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    NgoService ngoService;

    @Autowired
    NGORegisterRepository ngoRegisterRepository;

    @Autowired
    UserRepository userRepository;
    @GetMapping("/event")
    public String showEvents(Model model) {
        model.addAttribute("section", "event");
        List<Event> events = eventService.findAll(); 
        events.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));
        model.addAttribute("events", events);
        return "event";
    }

    @GetMapping("/ngoPage")
    public String showngo(Model model,HttpSession session,ModelMap map) {
         NgoRegisterDTO dto = (NgoRegisterDTO) session.getAttribute("ngoRegisterDTO");
         if (dto == null) {
        dto = new NgoRegisterDTO();
        dto.setName("Guest NGO");// default/fallback name
    }
         model.addAttribute("ngoRegisterDTO", dto);
        model.addAttribute("section", "ngo");
        return "dashboard.html";
    }


@GetMapping("/reports")
public String showReports(Model model, HttpSession session) {
    NgoRegisterDTO dto = (NgoRegisterDTO) session.getAttribute("ngoRegisterDTO");

    List<Map<String, Object>> reportCards = List.of();
    
    if (dto != null && dto.getEmail() != null) {
        // Get the full NGO entity using email
        NgoRegister ngo = ngoRegisterRepository.findByEmail(dto.getEmail());

        if (ngo != null) {
            List<ReportedPerson> persons = reportRepository.findByNgoId(ngo);
            
            reportCards = persons.stream().map(r -> {
                Map<String, Object> map = new HashMap<>();
                map.put("report", r);
                map.put("citizen", userRepository.findByPhone(r.getMobile()));
                return map;
            }).toList();
        }
    }
    model.addAttribute("reportCards", reportCards);
    System.out.println(reportCards.size()); // ✅ send to UI
    model.addAttribute("section", "report");
    return "reports";
}

       
    @PostMapping("/event")
    public String addEvent(@ModelAttribute Event event) {
        eventService.save(event);
        return "redirect:/ngo/home/event";
    }
    
    @PostMapping("/event/update")
public String updateEvent(@ModelAttribute Event event) {
    eventService.save(event);  // .save handles both insert/update
    return "redirect:/ngo/home/event";
}
@PostMapping("/event/delete/{id}")
public String deleteEvent(@PathVariable int id) {
    eventService.deleteById(id);  // Deletes event from DB
    return "redirect:/ngo/home/event";  // Reloads event page
}
    
}
