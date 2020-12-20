package com.blog.template.vo.topic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTopicReq {

    private Long topicId;

    @NotBlank(message = "please complete the title")
    private String title;

    @NotEmpty(message = "please complete the category")
    private Long categoryId;

}
