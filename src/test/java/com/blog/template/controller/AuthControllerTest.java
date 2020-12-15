package com.blog.template.controller;

import cn.hutool.json.JSONUtil;
import com.blog.template.ControllerBaseTest;
import com.blog.template.models.userinfo.UserInfo;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AuthControllerTest extends ControllerBaseTest {


    @Test
    public void register() throws Exception {
        UserInfo userInfo = UserInfo.builder()
                .password("123")
                .username("test")
                .build();
        String res = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(JSONUtil.toJsonStr(userInfo))
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(res);
    }



}
