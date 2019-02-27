package com.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class ScheduleConfig {

    private final static Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);

    public final static long ONE_DAY = 24 * 60 * 60 * 1000;

    public final static long ONE_HOUR = 60 * 60 * 1000;

    @Scheduled(fixedRate = ONE_DAY)
    public void scheduledTask1() {
        logger.info("scheduledTask1()");
    }

    @Scheduled(initialDelay = 1000, fixedRate = 5000)
    public void scheduledTask2() {
        logger.info("scheduledTask2()");
    }

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void ScheduledTask3() {
        logger.info("scheduledTask3()");
    }

}
