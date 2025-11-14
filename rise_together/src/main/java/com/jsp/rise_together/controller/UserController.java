package com.jsp.rise_together.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.rise_together.dto.ReportPersondto;
import com.jsp.rise_together.entity.User;
import com.jsp.rise_together.repository.UserRepository;
import com.jsp.rise_together.service.Implements.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user/home")
public class UserController {
    
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/userPage")
public String getUserpage(ModelMap map, HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");
    if (userId != null) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            map.put("user", user);
        } else {
            return "redirect:/login";
        }
    } else {
        return "redirect:/login";
    }
    return "userDashboard.html";
}

    @GetMapping("/notification")
    public String getNotification() {        
        return "userNotification.html";
    }
    @GetMapping("/addPeople")
    public String getAddpeople(ReportPersondto reportPersondto, ModelMap map,Model model) {        
        return  userService.loadreport(reportPersondto,map,model);
    }
    @PostMapping("/addPeople")
    public String reportMethod(@ModelAttribute @Valid ReportPersondto reportPersondto, BindingResult result, HttpSession session) {
        return userService.report(reportPersondto, result,session);
    }
    
    @GetMapping("/trackApplication")
    public String getTrackApplication() {        
        return "userTrackApplication.html";
    }
    @GetMapping("/rewards")
    public String getRewards() {        
        return "userRewards.html";
    }

    @PostMapping("/uploadProfile")
public String uploadProfileImage(@RequestParam("profileImage") MultipartFile file, HttpSession session) throws IOException {
    if (!file.isEmpty()) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        java.nio.file.Path uploadDir = Paths.get("uploads");

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Files.copy(file.getInputStream(), uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        // Get username from session instead of Principal
        String username = (String) session.getAttribute("username");
        if (username == null) {
            // User not logged in or session expired
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setProfileImagePath("/uploads/" + fileName);
            userRepository.save(user);
        }
    }
    return "redirect:/user/home/userPage";
}

}
