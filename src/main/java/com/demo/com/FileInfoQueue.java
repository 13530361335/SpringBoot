package com.demo.com;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FileInfoQueue {

    @RabbitListener(queues = "file_info")
    public void receiver1(byte[] message) {
        System.out.println("Receiver1  : " + new String(message));
    }

    @RabbitListener(queues = "file_info")
    public void receiver2(byte[] message) {
        System.out.println("Receiver2  : " + message);
    }

}