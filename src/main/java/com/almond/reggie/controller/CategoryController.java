package com.almond.reggie.controller;

import com.almond.reggie.Bean.Category;
import com.almond.reggie.common.R;
import com.almond.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping()
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加菜品成功");
    }

    /**
     * 分页查询菜单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 删除菜品, 如果当前分类关联了套餐则不能删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.remove(ids);
//        categoryService.removeById(id);
        return R.success("删除成功");
    }

    /**
     * 修改菜品
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getCreateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
