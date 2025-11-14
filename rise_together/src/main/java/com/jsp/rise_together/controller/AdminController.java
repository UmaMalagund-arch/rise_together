package com.jsp.rise_together.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.jsp.rise_together.entity.Event;
import com.jsp.rise_together.entity.NgoRegister;
import com.jsp.rise_together.repository.EventRepository;
import com.jsp.rise_together.repository.NGORegisterRepository;
import com.jsp.rise_together.service.Implements.EventService;
import com.jsp.rise_together.service.Implements.NgoService;

@Controller
@RequestMapping("/admin/home")
public class AdminController {

    @Autowired
    NGORegisterRepository ngoRegisterRepository;

    @Autowired
    NgoService ngoService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        long totalngo = ngoService.getTotalRegisteredNgo();
        long upcomingEvents = eventService.getUpcomingEventCount();
        model.addAttribute("totalNgo", totalngo);
        model.addAttribute("upcomingEvents", upcomingEvents);
        return "admin-home"; // dashboard.html in templates
    }

    @GetMapping("/manage-ngo")
    public String manageNGOPage(Model model) {
        List<NgoRegister> ngos = ngoRegisterRepository.findAll();
        List<Event> events= eventRepository.findAll();
        model.addAttribute("ngos", ngos);
        model.addAttribute("events", events);
        return "adminmanagengo.html";
    }

    @PostMapping("/approve-ngo")
    public String approveNgo(@RequestParam Long ngoId) {
        NgoRegister ngo = ngoRegisterRepository.findById(ngoId).orElseThrow();
        ngo.setApproved(true);
        ngoRegisterRepository.save(ngo);
        return "redirect:/admin/home/manage-ngo";
    }

    @PostMapping("/reject-ngo")
    public String rejectNgo(@RequestParam Long ngoId) {
        NgoRegister ngo = ngoRegisterRepository.findById(ngoId).orElseThrow();
        ngo.setApproved(false);
        ngoRegisterRepository.save(ngo);
        return "redirect:/admin/home/manage-ngo";
    }

    // ✅ NEW: View NGO document in browser
    @GetMapping("/view-document/{ngoId}")
    public ResponseEntity<Resource> viewDocument(@PathVariable Long ngoId) throws IOException {
        NgoRegister ngo = ngoRegisterRepository.findById(ngoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "NGO not found"));

        // adjust this according to your field name in entity (registrationProof / registrationProofPath)
        Path filePath = Paths.get(ngo.getRegistrationProof());

        if (!filePath.isAbsolute()) {
            filePath = Paths.get(System.getProperty("user.dir")).resolve(filePath).normalize();
        }

        if (!Files.exists(filePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + filePath);
        }

        Resource resource = new UrlResource(filePath.toUri());

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping("/notifications")
    public String notificationsPage() {
        return "adminNotification.html";
    }

    @GetMapping("/fundpay")
    public String fundPaymentsPage() {
        return "adminmanageDonation.html";
    }

}
