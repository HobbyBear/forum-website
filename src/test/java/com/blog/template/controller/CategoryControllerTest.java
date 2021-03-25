package com.blog.template.controller;

import com.blog.template.ControllerBaseTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class CategoryControllerTest extends ControllerBaseTest {

    @Test
    public void register() throws Exception {

        String res = mockMvc.perform(MockMvcRequestBuilders.get("/category/category_list")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(res);
    }

}
