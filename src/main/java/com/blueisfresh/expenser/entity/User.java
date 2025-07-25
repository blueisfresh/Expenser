package com.blueisfresh.expenser.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

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

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Column(name = "full_name", length = 100)
    private String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format") // Email well Formated
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @NotNull(message = "Creation timestamp cannot be null")
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull(message = "Update timestamp cannot be null")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // JPA Lifecycle Callbacks
    @PrePersist // Called before entity is first persisted
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now(); // Updated on creation as well
    }

    @PreUpdate // Called before entity is updated
    protected void onUpdate() {
        this.updatedAt = Instant.now(); // Update the timestamp on every update
    }

    // One User can have many Expenses
    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Expense> expenses;

    // One User can have many Categories
    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Category> categories;
}
