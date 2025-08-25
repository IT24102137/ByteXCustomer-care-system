package com.bytex.customercare.controller;

import com.bytex.customercare.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping("/customer/dashboard")
    public String customerDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Customer")) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "customer/dashboard";
    }
    
    @GetMapping("/staff/dashboard")
    public String staffDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Staff")) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "staff/dashboard";
    }
    
    // Similar methods for other roles...
}