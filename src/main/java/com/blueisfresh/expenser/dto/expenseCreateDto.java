package com.blueisfresh.expenser.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class expenseCreateDto {

    @NotBlank(message = "Expense title cannot be blank")
    @Size(min = 3, max = 100, message = "Expense title must be between 3 and 100 characters")
    private String title;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0") // Expense must have a positive amount
    private BigDecimal amount;

    @NotNull(message = "Date cannot be null")
    @PastOrPresent(message = "Expense date cannot be in the future") // Expense must be in the past or present
    private LocalDate expenseDate;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Category ID cannot be null") // Category is mandatory
    @Positive(message = "Category ID must be a positive number")
    private Long categoryId;
}
