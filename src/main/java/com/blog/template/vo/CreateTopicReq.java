package com.blog.template.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTopicReq {

    private int topicId;

    @NotBlank(message = "please complete the title")
    private String title;

    @NotBlank(message = "please complete the category")
    private Long categoryId;

}
