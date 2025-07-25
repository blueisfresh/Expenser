package com.blueisfresh.expenser.controller;

import com.blueisfresh.expenser.dto.categoryCreateDto;
import com.blueisfresh.expenser.entity.Category;
import com.blueisfresh.expenser.service.CategoryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Search Term
    @Transactional
    @GetMapping("{searchterm}")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String searchTerm) {
        List<Category> foundCategories = categoryService.searchCategories(searchTerm);
        if (!foundCategories.isEmpty()) {
            return ResponseEntity.ok(foundCategories);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Category> saveCategory(@Valid @RequestBody categoryCreateDto category) {
        Category createdCategory = categoryService.createCategory((category));
        // 201 successful creation
        return new ResponseEntity<>(categoryService.getById(createdCategory.getId()).orElseThrow(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody categoryCreateDto category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(categoryService.getById(updatedCategory.getId()).orElseThrow());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 HTTP for successful deletion
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
