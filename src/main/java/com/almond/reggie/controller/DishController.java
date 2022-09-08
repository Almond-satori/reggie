package com.almond.reggie.controller;

import com.almond.reggie.Bean.Category;
import com.almond.reggie.Bean.Dish;
import com.almond.reggie.Dto.DishDto;
import com.almond.reggie.common.R;
import com.almond.reggie.service.CategoryService;
import com.almond.reggie.service.DishFlavorService;
import com.almond.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        //我们需要分类名称,而Dish没有,在DisDto中封装这个字段
        Page<DishDto> dishDtoPage = new Page<>();
        //1.Dish的分页查询
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);
        //2.根据已有的分页查询结果,查询出分类名称
        //将分页数据中除了records的部分全部复制到dishDtoPage中
        BeanUtils.copyProperties(pageInfo, dishDtoPage,"records");
        //单独处理records
        List<Dish> records = pageInfo.getRecords();
        //通过流,将records每个item(Dish对象)和对categoryName的查询结果一起存入DishDto对象,并整理成list返回
        List<DishDto> dishDtos = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(dishDto.getCategoryId());
            String categoryName = category.getName();
            if (categoryName!=null) dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        //将新records放入新分页对象
        dishDtoPage.setRecords(dishDtos);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息,以及口味信息(修改菜单的显示原数据)
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品数据
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

}
