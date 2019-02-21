package com.demo.conf;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.demo.com.Constant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DruidConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = Constant.DB_PREFIX)
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Bean
    public ServletRegistrationBean druidServlet(DruidDataSource druidDataSource) {
        ServletRegistrationBean s = new ServletRegistrationBean();
        s.setServlet(new StatViewServlet());
        s.addUrlMappings("/druid/*");
        s.addInitParameter("loginUsername", druidDataSource.getUsername());
        s.addInitParameter("loginPassword", druidDataSource.getPassword());
        return s;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean f = new FilterRegistrationBean();
        f.setFilter(new WebStatFilter());
        f.addUrlPatterns("/*");
        f.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        f.addInitParameter("profileEnable", "true");
        f.addInitParameter("principalCookieName", "USER_COOKIE");
        f.addInitParameter("principalSessionName", "USER_SESSION");
        return f;
    }

}