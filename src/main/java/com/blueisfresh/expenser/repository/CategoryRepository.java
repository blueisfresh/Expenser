package com.blueisfresh.expenser.repository;

import com.blueisfresh.expenser.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Crud automatically generated
//    @Query("SELECT b FROM Category b WHERE " +
//            "LOWER(b.name) LIKE LOWER(CONCAT('%', :term, '%'))")
//    List<Category> findCategoriesBySearchTermBy(@Param("term") String term);

    // search Method
    List<Category> findByNameContainingIgnoreCase(String name);
}
