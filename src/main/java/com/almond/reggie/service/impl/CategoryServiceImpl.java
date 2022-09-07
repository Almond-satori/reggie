package com.almond.reggie.service.impl;


import com.almond.reggie.Bean.Category;
import com.almond.reggie.Bean.Dish;
import com.almond.reggie.Bean.Setmeal;
import com.almond.reggie.common.CustomException;
import com.almond.reggie.mapper.CategoryMapper;
import com.almond.reggie.service.CategoryService;
import com.almond.reggie.service.DishService;
import com.almond.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类,判断当前分类是否关联菜品,若关联则抛出异常
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishQueryWrapper);
        if(dishCount > 0){ //关联其他套餐
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(setmealQueryWrapper);
        if (setmealCount > 0){
            throw new CustomException("当前分类下关联了套餐,不能删除");
        }
        //正常删除
        super.removeById(id);
    }
}
