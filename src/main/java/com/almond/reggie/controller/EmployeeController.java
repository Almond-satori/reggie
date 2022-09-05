package com.almond.reggie.controller;

import com.almond.reggie.Bean.Employee;
import com.almond.reggie.common.R;
import com.almond.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee 从请求体中拿取数据,参数名对应对象中实例域的名字生成对象
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面传入的密码进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据界面提交的用户名查询数据库
        //包装类,包装了查询方法和查询参数
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);//获取到查询对象

        //3.如果查询失败直接返回
        if (emp == null){
            return R.error("用户名或密码错误");
        }
        //4.如果密码不匹配,登录失败
        if(!emp.getPassword().equals(password)){
            return R.error("用户名或密码错误");
        }
        //5.查看员工状态,是否被禁用
        if(emp.getStatus() == 0){
            return R.error("账号已被禁用");
        }
        //6.登录成功,将员工id放入session,并返回查询到的对象给前端
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return 返回成功信息(在success方法中会设置code=1)
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //删除当前会话中的employee信息
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 根据提交的表单,生成数据库记录,并初始化
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> addEmployee(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        //设置初始密码123456,使用md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获得当前登录用户的id以设置创建人
        long creatorId = (long) request.getSession().getAttribute("employee");
        employee.setCreateUser(creatorId);
        employee.setUpdateUser(creatorId);
        employeeService.save(employee);
        return R.success("新员工添加成功");
    }


}
