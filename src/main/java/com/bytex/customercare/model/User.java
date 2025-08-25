package com.bytex.customercare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users")  // Match exact case in SQL Server
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")  // Match exact case
    private Integer userId;

    @Column(name = "Username")  // Match exact case
    private String username;

    @Column(name = "Password")  // Match exact case
    private String password;

    @Column(name = "Email")  // Match exact case
    private String email;

    @Column(name = "FullName")  // Match exact case
    private String fullName;

    @Column(name = "PhoneNumber")  // Match exact case
    private String phoneNumber;

    @Column(name = "Role")  // Match exact case
    private String role;

    @Column(name = "CreatedAt")  // Match exact case - this was the problem
    private LocalDateTime createdAt;

    @Column(name = "LastLogin")  // Match exact case
    private LocalDateTime lastLogin;

    // Getters and setters remain the same
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
}