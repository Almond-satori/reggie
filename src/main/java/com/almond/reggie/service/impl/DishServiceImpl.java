package com.almond.reggie.service.impl;

import com.almond.reggie.Bean.Dish;
import com.almond.reggie.Bean.DishFlavor;
import com.almond.reggie.Dto.DishDto;
import com.almond.reggie.mapper.DishMapper;
import com.almond.reggie.service.DishFlavorService;
import com.almond.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;

    /**
     * 新增菜品,并将该菜品的口味信息(可能有多个)存储到对应表中
     * @param dishDto 封装了菜品和口味的对象
     */
    @Override
    @Transactional //涉及了多张表的操作,需要开启事务
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);//当我们将新菜品存入数据库,根据雪花算法生成了id
        //获取这个新生成的id
        Long dishId = dishDto.getId();
        //获取每个flavor,为其设置dishId
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
        //在数据库中存储所有flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 查询菜品和口味,封装在dto中
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询基本信息dish,设置到DishDto中
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        //查询口味信息,一个dish对应多个flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 更新菜品信息以及口味信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品信息
        this.updateById(dishDto);
        //更新口味信息,一个菜品对应多个口味,故先删除每个口味,然后再写入新口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //新增口味,拿到所有口味信息,设置其dishId,因为新建的口味没有id
        List<DishFlavor> flavors = dishDto.getFlavors();
        Long dishId = dishDto.getId();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);
    }
}
