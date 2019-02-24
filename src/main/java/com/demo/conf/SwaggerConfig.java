package com.demo.conf;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.SimpleDateFormat;
import java.util.Date;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.version}")
    private String appVersion;

    /**
     * Swagger配置
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title(appName + "服务接口展示")
                        .version(appVersion)
                        .description("发布时间: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()))
                        .license("登录Druid")
                        .licenseUrl("druid/index.html")
                        .build());
    }
}
