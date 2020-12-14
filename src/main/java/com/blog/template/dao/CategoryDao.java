package com.blog.template.dao;

import com.blog.template.models.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryDao extends JpaRepository<Category,Long>, JpaSpecificationExecutor<Category> {
}
