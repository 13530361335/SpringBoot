package com.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;


@Component
public class ShellUtil {

    private final static Logger logger = LoggerFactory.getLogger(ShellUtil.class);

    @Async
    public void execute(String... command) {
        Process process = null;
        BufferedReader reader = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            process.destroy();
        }

    }

}
