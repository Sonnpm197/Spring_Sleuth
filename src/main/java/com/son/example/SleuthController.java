package com.son.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

@RestController
public class SleuthController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public String helloSleuth() {
        logger.info("Hello Sleuth");
        return "success";
    }

    @Autowired
    private SleuthService sleuthService;

    @GetMapping("/same-span")
    public String helloSleuthSameSpan() throws InterruptedException {
        logger.info("Same Span");
        sleuthService.doSomeWorkSameSpan();
        return "success";
    }

    @GetMapping("/new-span")
    public String helloSleuthNewSpan() {
        logger.info("New Span");
        try {
            sleuthService.doSomeWorkNewSpan();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @Autowired
    private Executor executor;

    @GetMapping("/new-thread")
    public String helloSleuthNewThread() {
        logger.info("New Thread");
        Runnable runnable = () -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // log coming from the runnable has a unique span that will track the work
            // done in that thread because of the LazyTraceExecutor. if we were to use
            // a normal executor we would continue to see the same spanId used in the new thread.
            logger.info("I'm inside the new thread - with a new span");
        };
        executor.execute(runnable);

        logger.info("I'm done - with the original span");
        return "success";
    }

    @GetMapping("/async")
    public String helloSleuthAsync() {
        logger.info("Before Async Method Call");
        sleuthService.asyncMethod();
        logger.info("After Async Method Call");

        return "success";
    }
}