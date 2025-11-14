package com.jsp.rise_together.service.Implements;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.rise_together.dto.ReportPersondto;
import com.jsp.rise_together.entity.NgoRegister;
import com.jsp.rise_together.entity.ReportedPerson;
import com.jsp.rise_together.repository.NGORegisterRepository;
import com.jsp.rise_together.repository.ReportRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class UserService {

   @Autowired
   ReportRepository reportRepository;

   @Autowired
   NGORegisterRepository ngoRegisterRepository;

   @Autowired
   NgoService ngoService;

    public String loadreport(ReportPersondto reportPersondto, ModelMap map,Model model) {
        //System.out.println("********************************");
        map.put("reportPersondto", reportPersondto);
        
        List<NgoRegister> ngos=ngoService.getAllNgos();
        //System.out.println(ngos);
        model.addAttribute("ngos", ngos);
        return "userReportPeople.html";
    }

    public String report( @Valid ReportPersondto reportPersondto, BindingResult result, HttpSession session) {
    MultipartFile file=reportPersondto.getImage();
    if(file==null || file.isEmpty())
       result.rejectValue("image",null,"please upload image of person");
    
     if(result.hasErrors()){
        return "userReportPeople.html";
     }
     try{
         String uploadDir = "src/main/resources/uploads/Images/";
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
            NgoRegister ngo=ngoRegisterRepository.findById(reportPersondto.getNgoId()).orElseThrow(()-> new RuntimeException("Ngo not found"));
            ReportedPerson entity=new ReportedPerson(reportPersondto,ngo);
            entity.setImage(filePath);
            session.setAttribute("reportPersondto", reportPersondto);

             reportRepository.save(entity);

             return "redirect:/user/home";

     }catch (Exception e) {
            e.printStackTrace();
            result.rejectValue("image", null, "File upload failed. Try again.");
            return "userReportPeople.html";
        }

}


}
