package com.jsp.rise_together.service.Implements;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.rise_together.dto.NgoRegisterDTO;
import com.jsp.rise_together.entity.NgoRegister;
import com.jsp.rise_together.repository.NGORegisterRepository;
import com.jsp.rise_together.service.ServiceInterface;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class NGORegisterService implements ServiceInterface {
 
    @Autowired
    NGORegisterRepository ngoRegisterRepository;

    @Override
    public String getregister(NgoRegisterDTO ngoRegisterDTO, ModelMap map) {
        map.put("ngoRegisterDTO", ngoRegisterDTO);
        return "ngoRegistration.html";
    }

    @Override
    public String register(@Valid NgoRegisterDTO ngoRegisterDTO, BindingResult result, HttpSession session) {
        if (ngoRegisterRepository.existsByEmail(ngoRegisterDTO.getEmail()))
            result.rejectValue("email", "error.email", "*Email already Exists");
        if (ngoRegisterRepository.existsByRegisterNumber(ngoRegisterDTO.getRegisterNumber()))
            result.rejectValue("registerNumber", "error.registerNumber", "*RegisterNumber already Exists");
        if (ngoRegisterRepository.existsByName(ngoRegisterDTO.getName()))
            result.rejectValue("name", "error.name", "*Name already Exists");
        MultipartFile file = ngoRegisterDTO.getRegistrationProof();
        if (file == null || file.isEmpty()) {
            result.rejectValue("registrationProof", null, "Please upload the registration proof");
        }
        if (result.hasErrors())
            return "ngoRegistration.html";
        try {

            // 1. Save the file to disk
            String uploadDir = "src/main/resources/uploads/registration_proofs/";

            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
            String filePath = uploadDir + uniqueFilename;

            File directory = new File(uploadDir);
            if (!directory.exists())
                directory.mkdirs();

            File file2 = new File(filePath);

            try (FileOutputStream fos = new FileOutputStream(file2)) {
                fos.write(file.getBytes());
            }
            

        // Create Entity
        NgoRegister entity = new NgoRegister(ngoRegisterDTO);
        entity.setRegistrationProof(filePath);
        

            session.setAttribute("ngoRegisterDTO", ngoRegisterDTO);
            ngoRegisterRepository.save(entity);

            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace();
            result.rejectValue("registrationProof", null, "File upload failed. Try again.");
            return "ngoRegistration.html";
        }
    }

   

//     @Override
// public String login(String user, String password, HttpSession session) {
//     // if (user.equals(adminUsername) && password.equals(adminPassword)) {
//     //     return "redirect:/admin/home";
//     // }else{
//     //     Long mobile=null;
//     //     String email=null;
//     //     // try{
//     //     //     mobile=Long.parseLong(user);
//     //     //     // User user=UserRepository.find
//     //     // }
//     // }
//     if(user.equals("rakshita") && password.equals("raksha")){
//         return "redirect:/user/home";
//     }
//     NgoRegister n = null;
//     try {
//         n = ngoRegisterRepository.getByEmail(user);
//     } catch (NumberFormatException e) {
//         e.printStackTrace();
//     }

//     if (n == null) {
//         return "redirect:/login";
//     } else {
//         if (n.getRegisterNumber().equals(password)) {
//             NgoRegisterDTO dto = new NgoRegisterDTO();
//             dto.setEmail(n.getEmail());
//             dto.setName(n.getName());
//             dto.setTelephoneNumber(n.getTelePhoneNumber());
//             dto.setRegisterNumber(n.getRegisterNumber());
//             dto.setAddress(n.getAddress());
//             dto.setDescription(n.getDescription());

//             // ✅ Store the DTO in session
//             session.setAttribute("ngoRegisterDTO", dto);
//             return "redirect:/ngo/home";
//         } else {
//             return "redirect:/login";
//         }
//     }
// }

    
}