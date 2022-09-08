package com.almond.reggie.service.impl;

import com.almond.reggie.Bean.DishFlavor;
import com.almond.reggie.mapper.DishFlavorMapper;
import com.almond.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
