package com.almond.reggie.Dto;


import com.almond.reggie.Bean.Dish;
import com.almond.reggie.Bean.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据传输对象,包含了Dish对象中没有的字段
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
