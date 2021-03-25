package com.blog.template.controller;

import cn.hutool.json.JSONUtil;
import com.blog.template.BaseTest;
import com.blog.template.ControllerBaseTest;
import com.blog.template.models.userinfo.UserInfo;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static cn.hutool.http.HttpRequest.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TopicControllerTest extends ControllerBaseTest {

    @Test
    public void register() throws Exception {

        String res = mockMvc.perform(MockMvcRequestBuilders.get("/topic?topicId=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(res);
    }


}
