package com.almond.reggie.service.impl;

import com.almond.reggie.Bean.Employee;
import com.almond.reggie.mapper.EmployeeMapper;
import com.almond.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
