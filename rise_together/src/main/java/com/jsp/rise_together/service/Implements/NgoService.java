package com.jsp.rise_together.service.Implements;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.rise_together.dto.NgoRegisterDTO;
import com.jsp.rise_together.entity.Event;
import com.jsp.rise_together.entity.NgoRegister;
import com.jsp.rise_together.repository.NGORegisterRepository;

@Service
public class NgoService {

    public NgoRegisterDTO getLoggedInNgoDetails() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLoggedInNgoDetails'");
    }

    @Autowired
    private NGORegisterRepository ngoRegisterRepository;

    public long getTotalRegisteredNgo() {
        return ngoRegisterRepository.count();
    }

    public List<NgoRegister> getAllNgos() {
        
       return ngoRegisterRepository.findAll();
    
}

    public Event findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }
}
