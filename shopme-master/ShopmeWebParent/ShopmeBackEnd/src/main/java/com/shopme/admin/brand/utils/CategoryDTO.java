package com.shopme.admin.brand.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {

    private Integer id;
    private String name;

    public CategoryDTO(Integer id, String name){
        this.id=id;
        this.name=name;
    }
}
