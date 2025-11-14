package com.jsp.rise_together.service;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.jsp.rise_together.dto.NgoRegisterDTO;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



public interface ServiceInterface {

    String register(@Valid NgoRegisterDTO ngoRegisterDTO, BindingResult result, HttpSession session);

    String getregister(NgoRegisterDTO ngoRegisterDTO, ModelMap map);

   // String login(String user, String password,HttpSession session);

   

   

   

}
