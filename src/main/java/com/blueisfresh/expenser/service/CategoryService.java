package com.blueisfresh.expenser.service;

import com.blueisfresh.expenser.dto.categoryCreateDto;
import com.blueisfresh.expenser.entity.Category;
import com.blueisfresh.expenser.exception.ResourceNotFoundException;
import com.blueisfresh.expenser.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(categoryCreateDto categoryCreateDtoRequest) {
        Category category = new Category();
        category.setName(categoryCreateDtoRequest.getName());
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, categoryCreateDto categoryUpdateDtoRequest) {

        // find existing Category
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + categoryId + " not found."));

        existingCategory.setName(categoryUpdateDtoRequest.getName());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long categoryId) {
        // Check if it exists before deleting
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
        } else {
            throw new ResourceNotFoundException("Blog with ID " + categoryId + " not found for deletion.");
        }
    }

    public Optional<Category> getById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Search Term
    public List<Category> searchCategories(String searchTerm) {
        return categoryRepository.findBlogsBySearchTerm(searchTerm);
    }
}
