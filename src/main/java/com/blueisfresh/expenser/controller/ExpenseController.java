package com.blueisfresh.expenser.controller;

import com.blueisfresh.expenser.dto.expenseCreateDto;
import com.blueisfresh.expenser.entity.Expense;
import com.blueisfresh.expenser.exception.ResourceNotFoundException;
import com.blueisfresh.expenser.repository.ExpenseRepository;
import com.blueisfresh.expenser.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable("id") Long id) {
        Expense expense = expenseService.getExpenseById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + id + " not found for current user."));
        return ResponseEntity.ok(expense);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<Expense>> getExpensesByCategory(@RequestParam("name") String categoryName) {
        List<Expense> expensesFilteredByCategory = expenseService.getExpenseByCategory(categoryName);
        return ResponseEntity.ok(expensesFilteredByCategory);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Expense>> getExpensesBySearchTerm(@RequestParam("term") String searchTerm) {
        List<Expense> searchedExpenses = expenseService.searchExpense(searchTerm);
        return ResponseEntity.ok(searchedExpenses);
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody expenseCreateDto expenseCreateDtoRequest) {
        Expense createdExpense = expenseService.createExpense(expenseCreateDtoRequest);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED); // return http created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable("id") long id, @RequestBody expenseCreateDto expenseUpdateDtoRequest) {
        Expense updatedExpense = expenseService.updateExpense(id, expenseUpdateDtoRequest);
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 HTTP for successful deletion
    public void deleteExpense(@PathVariable("id") long id) {
        expenseService.deleteExpense(id);
    }

}
