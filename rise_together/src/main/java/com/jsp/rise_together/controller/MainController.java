package com.jsp.rise_together.controller;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.jsp.rise_together.dto.NgoRegisterDTO;
import com.jsp.rise_together.dto.UserRegisterDto;
import com.jsp.rise_together.entity.NgoRegister;
import com.jsp.rise_together.entity.User;
import com.jsp.rise_together.helper.AES;
import com.jsp.rise_together.repository.NGORegisterRepository;
import com.jsp.rise_together.repository.UserRepository;
import com.jsp.rise_together.service.ServiceInterface;
import com.jsp.rise_together.service.Implements.EmailService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController {

     @Value("${admin.username}")
    String adminUsername;

    @Value("${admin.password}")
    String adminPassword;
 
    @Autowired
    UserRepository userRepository;
    @Autowired
    ServiceInterface serviceInterface;
    @Autowired
    EmailService emailService;

    @Autowired
    NGORegisterRepository ngoRegisterRepository;

    @GetMapping("/")
    public String loadHome() {
        return "Home";
    }
   
    @GetMapping("/joinUs")
    public String joinUsload() {
        return "joinUs.html";
    }
    @GetMapping("/ngoregister")
    public String loadRegisterNGo(NgoRegisterDTO ngoRegisterDTO,ModelMap map){
        return serviceInterface.getregister(ngoRegisterDTO,map);
    }     
    @PostMapping("/ngoregister")
    public String postngoregister(@ModelAttribute @Valid NgoRegisterDTO ngoRegisterDTO,BindingResult result,HttpSession session) {   
     return serviceInterface.register(ngoRegisterDTO, result,session); 
    } 

    @GetMapping("/userregister")
    public String loadRegister(Model model) {
        model.addAttribute("userRegisterDto", new UserRegisterDto());
        return "UserRegister.html";
    }

    @PostMapping("/userregister")
    public String registerUser(@ModelAttribute("userRegisterDto") @Valid UserRegisterDto userRegisterDto,
                               BindingResult result, Model model, HttpSession session) {
        
        if(userRepository.existsByEmail(userRegisterDto.getEmail()))
           result.rejectValue("email","error.email", "*Email already Exists");
        if(userRepository.existsByPhone(userRegisterDto.getPhone()))
           result.rejectValue("phone", "error.phone", "*Mobile number already Exists");
        if(!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword()))
           result.rejectValue("confirmPassword","error.confirmPassword","Password and Confirm Password must be the same!");
          
        
        if (result.hasErrors()) {
            return "UserRegister.html";
        }

        // Convert DTO to Entity
        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setPhone(userRegisterDto.getPhone());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(AES.encrypt(userRegisterDto.getPassword()));
        user.setAddress(userRegisterDto.getAddress());
        user.setVerified(false);

        session.setAttribute("userRegisterDto", userRegisterDto);
        userRepository.save(user);

        // Generate OTP
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        LocalDateTime otpExpiryTime = LocalDateTime.now().plusMinutes(5);

        // Send OTP
        emailService.sendOtpEmail(user.getEmail(), otp);

        // Store in session
        session.setAttribute("otp", otp);
        session.setAttribute("otpExpiry", otpExpiryTime);
        session.setAttribute("userId", user.getId());

        return "otpVerify.html";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String enteredOtp,
                             HttpSession session, Model model) {
        String sessionOtp = (String) session.getAttribute("otp");
        LocalDateTime otpExpiry = (LocalDateTime) session.getAttribute("otpExpiry");
        Long userId = (Long) session.getAttribute("userId");

        if (sessionOtp == null || userId == null) {
            model.addAttribute("error", "Session expired. Please register again.");
            return "userRegister.html";
        }

        if (LocalDateTime.now().isAfter(otpExpiry)) {
            model.addAttribute("error", "OTP expired. Please request again.");
            return "otpVerify.html";
        }

        if (sessionOtp.equals(enteredOtp)) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                user.setVerified(true);
                userRepository.save(user);
            }
            session.invalidate(); // Clear session
            return "Home";
        } else {
            model.addAttribute("error", "Invalid OTP! Try again.");
            return "otpVerify.html";
        }
    }

    @GetMapping("/resend-otp")
    public String resendOtp(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "userRegister.html";
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
            LocalDateTime otpExpiryTime = LocalDateTime.now().plusMinutes(5);
            emailService.sendOtpEmail(user.getEmail(), otp);
            session.setAttribute("otp", otp);
            session.setAttribute("otpExpiry", otpExpiryTime);
            model.addAttribute("message", "OTP resent successfully!");
        }

        return "otpVerify.html";
    }

    @GetMapping("/login") 
    public String loadLoginNGO(ModelMap map){ 
        return "Login.html"; 
    } 
    @PostMapping("/login")
    public String postlogin(@RequestParam String  user,@RequestParam String  password,HttpSession session){
         if (user.equals(adminUsername) && password.equals(adminPassword)) {
             return "redirect:/admin/home/dashboard";
         }
        else{
            Long phone=null;
            String email=null;
            try{
                phone=Long.parseLong(user);
                User userdata=userRepository.findByPhone(phone);
                if(userdata==null){
                    session.setAttribute("error", "Invalid Mobile Number");
                    return "redirect:/login";
                }else{
                    if(userdata!=null){
                        if(AES.decrypt(userdata.getPassword()).equals(password)){
                             session.setAttribute("success", "Login Success as User");
                            session.setAttribute("userdata", userdata);
                            session.setAttribute("username", userdata.getUsername()); 
                            session.setAttribute("userId", userdata.getId());	
                            return "redirect:/user/home";
                        }else{
                           session.setAttribute("error", "Invalid password");
                             return "redirect:/login";
                        }
                    }
                }
            }catch(NumberFormatException e){
                email=user;
                User userdata=userRepository.findByEmail(email);
                NgoRegister ngo=ngoRegisterRepository.findByEmail(email);
                if(userdata==null && ngo==null){
                    session.setAttribute("error", "Invalid Email");
                    return "redirect:/login";
                }else{
                    if(userdata!=null){ 
                        if(AES.decrypt(userdata.getPassword()).equals(password)){
                            session.setAttribute("success", "Login Success as User");
                            session.setAttribute("username", userdata.getUsername()); 
                            session.setAttribute("userId", userdata.getId());
							return "redirect:/user/home";  
                        }else{
                            session.setAttribute("error", "Invalid Password");
							return "redirect:/login";
                        }
                    }else{
                        if (!ngo.isApproved()) { // if NGO is not approved
                           session.setAttribute("error", "Your account is not yet approved by admin.");
                        return "redirect:/login";
                         }
                        if(ngo.getRegisterNumber().equals(password)){
                            NgoRegisterDTO dto = new NgoRegisterDTO();
                            dto.setName(ngo.getName());
                            dto.setEmail(ngo.getEmail());
                            dto.setAddress(ngo.getAddress());
                            dto.setDescription(ngo.getDescription());
                            dto.setPersonMobile(ngo.getPersonMobile());
                            dto.setPersonName(ngo.getPersonName());
                            dto.setRegisterDate(ngo.getRegisterDate());
                            dto.setRegisterNumber(ngo.getRegisterNumber());
                            dto.setPinCode(ngo.getPinCode());
                            dto.setRegistrationType(ngo.getRegistrationType());

                            session.setAttribute("success", "Login Success as Ngo");
							 session.setAttribute("ngo", ngo);
                            session.setAttribute("ngoRegisterDTO", dto);
							return "redirect:/ngo/home";
                        }else{
                            session.setAttribute("error", "Invalid Registerer Number");
							return "redirect:/login";
                        }
                    }
                }
            }
         return "redirect:/login";

        }
    }
   
    @GetMapping("/ngo/home")
    public String showNgoHome(HttpSession session, ModelMap map) {
    NgoRegisterDTO dto = (NgoRegisterDTO) session.getAttribute("ngoRegisterDTO");
    map.put("ngoRegisterDTO", dto); 
    return "ngo-home";
    }

    @GetMapping("/user/home")
    public String loadUserHome(ModelMap map,HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");
     if (userId != null) {
        User user = userRepository.findById(userId).orElse(null);
        map.put("user", user); // Pass entity directly
    } else {
        // Handle case where no user is logged in
        map.put("error", "Please log in first.");
        return "redirect:/login";
    }
        return "user-home.html";
    }
    

    @GetMapping("/admin/home")
    public String loadAdminHome(){
        return "admin-home.html";
    }
    
    @GetMapping("/admin/view-ngos")
    public String getMethodName() {
        return new String();
    } 

    @GetMapping("/about")
    public String getAboutUs() {
        return "AboutUs.html";
    }
    @GetMapping("/life")
    public String getLife() {
        return "life.html";
    }
    @GetMapping("/life/elderly")
    public String getElderly() {
        return "elderly.html";
    }
    @GetMapping("/life/hunger")
    public String getHunger() {
        return "hunger.html";
    }
    @GetMapping("/life/children")
    public String getChhildren() {
        return "Children.html";
    }
    @GetMapping("/life/specialy-abled")
    public String getSpecialyAbled() {
        return "specialyAbled.html";
    }

    @GetMapping("/donate")
    public String getDonation() {
        return "donation.html";
    }
    
    
}   
