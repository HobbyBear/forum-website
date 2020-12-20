package com.blog.template.dao;

import com.blog.template.models.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoryDao extends JpaRepository<Category,Long>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByName(String name);
}
