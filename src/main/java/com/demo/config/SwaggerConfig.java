package com.demo.config;

import com.demo.common.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
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

    @Value("${info.version}")
    private String version;

    /**
     * Swagger配置
     *
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(Constant.CONTROLLER_PATH))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title(appName + "接口展示")
                        .version(version)
                        .description("发布时间: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()))
                        .build());
    }
}
