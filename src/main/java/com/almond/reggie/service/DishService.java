package com.almond.reggie.service;

import com.almond.reggie.Bean.Dish;
import com.almond.reggie.Dto.DishDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {
    //在dish表存储dish,在dishFlavor表中存储flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品已经口味信息
    public void updateWithFlavor(DishDto dishDto);
}
