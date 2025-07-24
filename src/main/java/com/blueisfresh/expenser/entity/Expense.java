package com.blueisfresh.expenser.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_expense")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    @NotBlank(message = "Expense title cannot be blank")
    @Size(min = 3, max = 100, message = "Expense title must be between 3 and 100 characters")
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0") // Expense must have a positive amount
    @Column(name = "amount", nullable = false, precision = 10, scale = 2) // Precision and scale for currency
    private BigDecimal amount;

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Expense date cannot be in the future") // Expense must be in the past or present
    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

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

    // Many Expenses can have one Category
    @JsonManagedReference
    @NotNull(message = "Expense must be associated with a category")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Many Expenses can have one User
    @JsonManagedReference
    @NotNull(message = "Expense must be associated with a user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
