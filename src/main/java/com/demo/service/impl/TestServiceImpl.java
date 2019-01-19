package com.demo.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.stream.IntStream;

@Component
public class TestServiceImpl {

    @Async
    public void sendSms() {
        System.out.println(Thread.currentThread().getName());
        System.out.println("####sendSms####   2");
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
