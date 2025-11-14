// package com.jsp.rise_together.helper;


// import org.springframework.security.core.Authentication;
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import java.io.IOException;

// @Component
// public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request,
//                                         HttpServletResponse response,
//                                         Authentication authentication)
//                                         throws IOException, ServletException {

//         String role = authentication.getAuthorities().toString();

//         if (role.contains("ROLE_NGO")) {
//             response.sendRedirect("/ngo-dashboard");
//         } else if (role.contains("ROLE_CITIZEN")) {
//             response.sendRedirect("/user-dashboard");
//         } else {
//             response.sendRedirect("/home");
//         }
//     }
// }

