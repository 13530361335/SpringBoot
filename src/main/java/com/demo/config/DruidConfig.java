package com.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Druid配置
 */
@Configuration
public class DruidConfig {

    private static Logger log = LoggerFactory.getLogger(DruidConfig.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean s = new ServletRegistrationBean();
        s.setServlet(new StatViewServlet());
        s.addUrlMappings("/druid/*");
        s.addInitParameter("loginUsername", username);
        s.addInitParameter("loginPassword", password);
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

    @Bean
    @Primary
    public DataSource druidDataSource() {
        DruidDataSource d = new DruidDataSource();
        d.setUrl(url);
        d.setUsername(username);
        d.setPassword(password);
        d.setDriverClassName(driverClassName);
        d.setInitialSize(initialSize);
        d.setMaxActive(maxActive);
        d.setMaxWait(maxWait);
        d.setValidationQuery(validationQuery);
        try {
            d.setFilters(filters);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return d;
    }

}