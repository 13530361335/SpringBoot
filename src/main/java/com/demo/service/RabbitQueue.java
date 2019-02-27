package com.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitQueue {

    @RabbitListener(queues = "file_info")
    public void receiver1(byte[] message) {
        System.out.println("Receiver: " + new String(message));
    }

}