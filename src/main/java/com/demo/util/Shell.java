package com.demo.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class Shell {


    @Async
    public void doSomeThing() {
        Process process;
        try {
            process = Runtime.getRuntime().exec("ping baidu.com");
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
