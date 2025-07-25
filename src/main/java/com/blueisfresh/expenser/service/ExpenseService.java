package com.blueisfresh.expenser.service;

import com.blueisfresh.expenser.dto.expenseCreateDto;
import com.blueisfresh.expenser.entity.Category;
import com.blueisfresh.expenser.entity.Expense;
import com.blueisfresh.expenser.entity.User;
import com.blueisfresh.expenser.exception.ResourceNotFoundException;
import com.blueisfresh.expenser.repository.CategoryRepository;
import com.blueisfresh.expenser.repository.ExpenseRepository;
import com.blueisfresh.expenser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    CategoryService categoryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Transactional
    public Expense createExpense(expenseCreateDto expenseCreateDtoRequest) {
        Expense expense = new Expense();

        // Find Category
        Category category = categoryService.getCategory(expenseCreateDtoRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + expenseCreateDtoRequest.getCategoryId() + " not found."));
        expense.setCategory(category);

        // Find User
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user '" + username + "' not found in database."));

        if (user == null) {
            throw new UsernameNotFoundException("Authenticated user not found in database: " + username);
        }
        expense.setUser(user);

        // Populate expense entity fields from the dto
        expense.setTitle(expenseCreateDtoRequest.getTitle());
        expense.setAmount(expenseCreateDtoRequest.getAmount());
        expense.setExpenseDate(expenseCreateDtoRequest.getExpenseDate());
        expense.setDescription(expenseCreateDtoRequest.getDescription());

        // save new Expense in the database
        return expenseRepository.save(expense);
    }

    @Transactional
    public Expense updateExpense(Long expenseId, expenseCreateDto expenseUpdateDtoRequest) {
        // verfiy ownership of the expense being updated
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user '" + username + "' not found."));

        // find existing Expense
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + expenseId + " not found."));

        // Check if the authenticated user owns this expense
        if (!existingExpense.getUser().getId().equals(currentUser.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("User is not authorized to update expense with ID " + expenseId);
        }

        // Find Category
        Category category = categoryService.getCategory(expenseUpdateDtoRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + expenseUpdateDtoRequest.getCategoryId() + " not found."));
        existingExpense.setCategory(category);

        existingExpense.setTitle(expenseUpdateDtoRequest.getTitle());
        existingExpense.setAmount(expenseUpdateDtoRequest.getAmount());
        existingExpense.setExpenseDate(expenseUpdateDtoRequest.getExpenseDate());
        existingExpense.setDescription(expenseUpdateDtoRequest.getDescription());

        return expenseRepository.save(existingExpense);
    }

    @Transactional
    public void deleteExpense(Long expenseId) {
        // verfiy ownership of the expense being updated
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user '" + username + "' not found."));

        Expense expenseToDelete = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + expenseId + " not found for deletion."));

        // Check if the authenticated user owns this expense
        if (!expenseToDelete.getUser().getId().equals(currentUser.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("User is not authorized to delete expense with ID " + expenseId);
        }

        expenseRepository.delete(expenseToDelete);
    }

    @Transactional(readOnly = true)
    public Optional<Expense> getExpenseById(Long expenseId) {
        // verfiy ownership of the expense being updated
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user '" + username + "' not found."));

        Optional<Expense> expense = expenseRepository.findById(expenseId);

        // Check if the expense exists AND belongs to the current user
        if (expense.isPresent() && !expense.get().getUser().getId().equals(currentUser.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("User is not authorized to view expense with ID " + expenseId);
        }

        return expense;
    }

    public List<Expense> getExpenseByCategory(String categoryName) {
// Find the current authenticated user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user '" + username + "' not found in database."));

        return expenseRepository.findByUserIdAndCategoryNameIgnoreCase(currentUser.getId(), categoryName);
    }

    @Transactional(readOnly = true)
    public List<Expense> getAllExpenses() {
// Only retrieve expenses from the current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user '" + username + "' not found."));

        return expenseRepository.findByUserId(currentUser.getId());
    }

    // Search Term
    public List<Expense> searchExpense(String searchTerm) {
        // search expenses only for the current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user '" + username + "' not found."));

        return expenseRepository.findByUserIdAndTitleContainingIgnoreCase(currentUser.getId(), searchTerm);
    }

}
