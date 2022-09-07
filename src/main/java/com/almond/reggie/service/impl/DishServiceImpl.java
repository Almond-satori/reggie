package com.almond.reggie.service.impl;

import com.almond.reggie.Bean.Dish;
import com.almond.reggie.mapper.DishMapper;
import com.almond.reggie.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
