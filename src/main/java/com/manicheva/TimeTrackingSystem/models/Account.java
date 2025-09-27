package com.manicheva.TimeTrackingSystem.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @Column(name="account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name="username", unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String username;

    @Column(name="password")
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
