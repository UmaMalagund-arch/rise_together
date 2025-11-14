package com.jsp.rise_together.service.Implements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.rise_together.entity.NgoRegister;
import com.jsp.rise_together.repository.NGORegisterRepository;

@Service
public class AdminService {

    @Autowired
    NGORegisterRepository ngoRegisterRepository;
    public void approveNgo(Long ngoId) {
    NgoRegister ngo = ngoRegisterRepository.findById(ngoId).orElseThrow();
    ngo.setApproved(true);
    ngoRegisterRepository.save(ngo);
}

}
