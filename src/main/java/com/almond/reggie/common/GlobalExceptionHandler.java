package com.almond.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理类,方法返回响应体
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
//      error message:Duplicate entry '123456' for key 'idx_username'
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")){
            //按照" "将字符创分为小串 eg:split[0]==Duplicate
            String[] split = message.split(" ");
            String msg = "用户名" + split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
}
