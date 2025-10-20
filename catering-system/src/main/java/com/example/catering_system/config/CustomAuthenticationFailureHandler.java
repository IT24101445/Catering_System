package com.example.catering_system.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        
        // Get the referer to determine which login page was used
        String referer = request.getHeader("Referer");
        
        System.out.println("Login failed. Referer: " + referer);
        
        // Check if login came from customer-admin login page
        if (referer != null && referer.contains("/login/customer-admin")) {
            // Redirect to customer admin login page with error
            System.out.println("Redirecting to /login/customer-admin?error");
            response.sendRedirect(request.getContextPath() + "/login/customer-admin?error");
        } else {
            // Redirect to regular customer login page with error
            System.out.println("Redirecting to /login/customer?error");
            response.sendRedirect(request.getContextPath() + "/login/customer?error");
        }
    }
}
