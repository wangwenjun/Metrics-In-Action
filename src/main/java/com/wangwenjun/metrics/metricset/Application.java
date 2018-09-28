package com.wangwenjun.metrics.metricset;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

public class Application
{
    private final static MetricRegistry registry = new MetricRegistry();

    private final static JmxReporter reporter = JmxReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws InterruptedException
    {
        reporter.start();
        BusinessService businessService = new BusinessService();
        registry.registerAll(businessService);
        businessService.start();
        Thread.currentThread().join();
    }
}