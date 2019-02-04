package com.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.stream.IntStream;

@Component
public class TestServiceImpl {

    private final static Logger log = LoggerFactory.getLogger(TestServiceImpl.class);

    @Async
    public void sendSms() {
        log.info(Thread.currentThread().getName());
        IntStream.range(0, 5).forEach(d -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("####sendSms####   3");
    }
}
