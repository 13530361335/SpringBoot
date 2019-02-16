package com.demo.com;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public final static String File_Info = "file_info";

    @Bean
    public Queue queue() {
        return new Queue(File_Info, true);
    }


}

