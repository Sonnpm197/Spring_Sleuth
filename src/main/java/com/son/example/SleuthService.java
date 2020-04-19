package com.son.example;

import brave.Span;
import brave.Tracer;
import brave.sampler.Sampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
//import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
//import org.springframework.cloud.sleuth.zipkin.HttpZipkinSpanReporter;
//import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
//import org.springframework.cloud.sleuth.zipkin.ZipkinSpanReporter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.client.RestTemplate;

@Service
public class SleuthService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private SpanMetricReporter spanMetricReporter;
//
//    @Autowired
//    private ZipkinProperties zipkinProperties;
//
//
//
//    @Bean
//    public ZipkinSpanReporter makeZipkinSpanReporter() {
//        return new ZipkinSpanReporter() {
//            private HttpZipkinSpanReporter delegate;
//
//            @Override
//            public void report(zipkin.Span span) {
//                delegate = new HttpZipkinSpanReporter(
//                        new RestTemplate(), "http://localhost:9411", zipkinProperties.getFlushInterval(), spanMetricReporter);
//                if (!span.name.matches("(^cleanup.|.+favicon.)")) {
//                    delegate.report(span);
//                }
//            }
//        };
//    }

    public void doSomeWorkSameSpan() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Doing some work");
    }

    @Autowired
    private Tracer tracer;
    public void doSomeWorkNewSpan() throws InterruptedException {
        logger.info("I'm in the original span");

        Span newSpan = tracer.nextSpan().name("newSpan").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(newSpan.start())) {
            Thread.sleep(1000L);
            logger.info("I'm in the new span doing some cool work that needs its own span");
        } finally {
            newSpan.finish();
        }

        logger.info("I'm in the original span");
    }

    @Async
    public void asyncMethod() {
        logger.info("Start Async Method");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("End Async Method");
    }
}
