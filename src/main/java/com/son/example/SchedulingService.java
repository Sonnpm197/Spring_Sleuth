package com.son.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulingService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SleuthService sleuthService;

    @Scheduled(fixedDelay = 3000)
    public void scheduledWork() throws InterruptedException {
        logger.info("Start some work from the scheduled task");
        sleuthService.asyncMethod();
        logger.info("End work from scheduled task");
    }
}
