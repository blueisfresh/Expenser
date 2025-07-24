package com.blueisfresh.expenser.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
@Table(name = "tbl_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

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

    // One Category can have many Expenses
    @JsonBackReference
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Expense> expenses;

    // Many Categories can have one User
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
