package com.blueisfresh.expenser.repository;

import com.blueisfresh.expenser.entity.Category;
import com.blueisfresh.expenser.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Crud Operations generated

    @Query("SELECT e FROM Expense e WHERE " +
            "LOWER(e.title) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(CAST(e.amount AS string)) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(CAST(e.expenseDate AS string)) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Expense> findExpensesBySearchTerm(@Param("term") String term);

    List<Expense> findByUserId(Long id);

    List<Expense> findByUserIdAndTitleContainingIgnoreCase(Long id, String searchTerm);

    List<Expense> findByUserIdAndCategoryNameIgnoreCase(Long userId, String categoryName);
}
