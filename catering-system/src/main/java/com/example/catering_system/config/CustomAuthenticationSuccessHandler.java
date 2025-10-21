package com.example.catering_system.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        // Get the referer to determine which login page was used
        String referer = request.getHeader("Referer");
        
        System.out.println("Login successful. Referer: " + referer);
        
        // Check if login came from customer-admin login page
        if (referer != null && referer.contains("/login/customer-admin")) {
            // Redirect to customer dashboard for admin users
            System.out.println("Redirecting to /dashboard-customer");
            response.sendRedirect(request.getContextPath() + "/dashboard-customer");
        } else {
            // Redirect regular customers to the main (Thymeleaf) home page
            System.out.println("Redirecting to /customer (main customer home)");
            response.sendRedirect(request.getContextPath() + "/customer");
        }
    }
}
