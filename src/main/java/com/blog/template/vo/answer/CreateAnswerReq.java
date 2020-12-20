package com.blog.template.vo.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAnswerReq {

    @NotBlank
    private Long topicId;

    @NotBlank(message = "please complete the content")
    private String content;

    @NotBlank
    private String contentText;

}
