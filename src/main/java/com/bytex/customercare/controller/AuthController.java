package com.bytex.customercare.controller;

import com.bytex.customercare.model.User;
import com.bytex.customercare.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
    
    @PostMapping("/login")
    public String processLogin(@RequestParam String username, 
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        
        if (userService.authenticate(username, password)) {
            User user = userService.findByUsername(username);
            session.setAttribute("user", user);
            
            // Redirect based on user role
            switch (user.getRole()) {
                case "Customer":
                    return "redirect:/customer/dashboard";
                case "Staff":
                    return "redirect:/staff/dashboard";
                case "Technician":
                    return "redirect:/technician/dashboard";
                case "ProductManager":
                    return "redirect:/product-manager/dashboard";
                case "WarehouseManager":
                    return "redirect:/warehouse-manager/dashboard";
                case "Admin":
                    return "redirect:/admin/dashboard";
                default:
                    return "redirect:/login";
            }
        }
        
        model.addAttribute("error", "Invalid username or password");
        return "auth/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout";
    }
}