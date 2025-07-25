package com.blueisfresh.expenser.repository;

import com.blueisfresh.expenser.entity.Category;
import com.blueisfresh.expenser.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Crud Operations generated

    @Query("SELECT b FROM Expense b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(b.amount) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(b.expenseDate) LIKE LOWER(CONCAT('%', :term, '%'))"
    )
    List<Expense> findExpensesBySearchTerm(@Param("term") String term);

    List<Expense> findByUserId(Long id);

    List<Expense> findByUserIdAndTitleContainingIgnoreCase(Long id, String searchTerm);

    List<Expense> findByUserIdAndCategoryNameIgnoreCase(Long userId, String categoryName);
}
