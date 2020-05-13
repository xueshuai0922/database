package com.xs.database.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author 薛帅
 * @Date 2019/4/13 10:18
 * @Description 增加过滤器 对请求进行判断 都返回到index2上
 *             也可以请求的时候，把请求头requestMapping（/*）,但是并不想这么做，就是皮
 */
@Configuration
public class DataBaseFilterConfig  {
    @Bean
    public FilterRegistrationBean getFilter(){
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new DataBaseFilter() );
        filterBean.setUrlPatterns(Arrays.asList("/*"));
        return filterBean;
    }

}
class  DataBaseFilter implements  Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest =(HttpServletRequest)request;
        HttpServletResponse httpServletResponse =(HttpServletResponse)response;
        String uri = httpServletRequest.getRequestURI();
        boolean contains = uri.contains("/getTableList");
        boolean conUri = uri.contains("/testConnection");
        if(contains ||conUri || uri.endsWith("js")
                ||uri.endsWith("css")
                ||uri.endsWith("png")
                ||uri.endsWith("ico")
                ||uri.endsWith("woff")
                ||uri.endsWith("ttf")
                ||uri.contains("druid")){
            chain.doFilter(request,response);
        }
        else {
            httpServletRequest.getRequestDispatcher("/").forward(httpServletRequest,httpServletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
