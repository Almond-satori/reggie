package com.almond.reggie.controller;

import com.almond.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;//D://reggieImg

    /**
     * 捕获上传文件的请求
     * @param file 跟请求中form-data中的name字段一致
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是一个临时文件(.tmp),在方法结束后file就会被删除
        //1.处理文件名
        //获取原始文件名
        String originalName = file.getOriginalFilename();
        //获取后缀".jpg"
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        //使用uuid重新生成文件名,防止重名
        String fileName = UUID.randomUUID().toString() + suffix;
        //2.创建存储目录
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdir();
        }
        //3.转存文件
        try {
            file.transferTo(new File(basePath+"/"+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        FileInputStream fileInputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(basePath + "/" + name));
            servletOutputStream = response.getOutputStream();
            response.setContentType("/image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while( (len = fileInputStream.read(bytes)) != -1 ){
                servletOutputStream.write(bytes,0,len);
                servletOutputStream.flush();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream!=null) {
                try {
                    fileInputStream.close();
                    servletOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
