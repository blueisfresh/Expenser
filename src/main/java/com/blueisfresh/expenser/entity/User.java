package com.blueisfresh.expenser.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 64, message = "Username must be between 3 and 64 characters")
    @Column(name = "username", nullable = false, length = 64, unique = true)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    // TODO: Password Complexity validator
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Column(name = "full_name", length = 100)
    private String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format") // Email well Formated
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;
}
