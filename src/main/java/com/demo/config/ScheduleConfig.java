package com.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class ScheduleConfig {

    @Autowired
    private static Logger log = LoggerFactory.getLogger(ScheduleConfig.class);

    public final static long ONE_DAY = 24 * 60 * 60 * 1000;

    public final static long ONE_HOUR = 60 * 60 * 1000;

    @Scheduled(fixedRate = ONE_DAY)
    public void scheduledTask() {
        log.info("初始执行,间隔1D");
    }

    @Scheduled(fixedDelay = ONE_HOUR)
    public void scheduleTask2() {
        log.info("初始执行,间隔1H");
    }

    @Scheduled(initialDelay = 1000, fixedRate = 5000)
    public void doSomething() {
        // something that should execute periodically
    }

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void ScheduledTask3() {
        log.info("间隔1M");
    }

}
