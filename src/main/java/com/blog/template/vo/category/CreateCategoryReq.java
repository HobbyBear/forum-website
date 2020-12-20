package com.blog.template.vo.category;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryReq {
    private Long categoryId;

    @NotBlank(message = "please complete the title")
    private String title;

}
