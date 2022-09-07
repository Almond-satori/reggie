package com.almond.reggie.service;

import com.almond.reggie.Bean.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
