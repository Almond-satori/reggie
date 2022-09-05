package com.almond.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.almond.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//设置过滤器名字和拦截pattern,在main类中设置@ServletComponentScan进行组件扫描
@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
@Slf4j
public class LoginFilter implements Filter {
    //路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //1.获取本次请求的URI,这里URI(/employee/login)和URL(http://localhost:8080/backend/page/login/login.html)完全不同
        String requestURI = request.getRequestURI();
        //定义不需要处理的请求路径,登录登出不需要,静态页面也不需要(数据都是动态ajax向服务端的数据库请求的)
        String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**"
        };
        //2.判断是否需要登录验证,不需要登录则返回
        boolean check = checkURLs(urls, requestURI);
        if(check){
            filterChain.doFilter(request, response);
            return;//注意不要忘记返回
        }
        //3.若需要登录,就判断登录状态,如果登录了就放行
        if(request.getSession().getAttribute("employee") != null){
            filterChain.doFilter(request, response);
            return;//注意不要忘记返回
        }
        //4.未登录,则根据前端的要求(request.js),设置响应
        //以输出流响应前端
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 判断requestURL是否是不需要验证登录的url
     * @param urls
     * @param requestURI
     * @return
     */
    private boolean checkURLs(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url ,requestURI);
            if(match)
                return true;
        }
        return false;
    }
    

}
