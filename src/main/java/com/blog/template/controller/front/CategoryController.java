package com.blog.template.controller.front;

import com.blog.template.common.annotation.PassToken;
import com.blog.template.common.annotation.UserLoginToken;
import com.blog.template.dao.CategoryDao;
import com.blog.template.exceptions.CustomerException;
import com.blog.template.models.category.Category;
import com.blog.template.vo.ResponseMsg;
import com.blog.template.vo.category.CreateCategoryReq;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;


    @ApiOperation("create category")
    @PostMapping
    @UserLoginToken
    public ResponseMsg createTopic(@RequestBody CreateCategoryReq categoryReq) {
        if (categoryDao.findByName(categoryReq.getTitle()).isPresent()) {
            throw new CustomerException("the category title has exited!");
        }

        Category topic = Category.builder()
                .name(categoryReq.getTitle())
                .build();

        categoryDao.save(topic);

        return ResponseMsg.success200("create category success");
    }


    @ApiOperation("category list")
    @GetMapping("category_list")
    @PassToken
    public ResponseMsg categoryList() {
        List<Category> categoryList = categoryDao.findAll();
        return ResponseMsg.success200(categoryList);
    }



}
