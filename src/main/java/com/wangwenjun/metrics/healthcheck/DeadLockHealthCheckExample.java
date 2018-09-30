package com.wangwenjun.metrics.healthcheck;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;

import java.util.concurrent.TimeUnit;

public class DeadLockHealthCheckExample
{
    public static void main(String[] args) throws InterruptedException
    {
        final HealthCheckRegistry hcRegistry = new HealthCheckRegistry();
        hcRegistry.register("thread-dead-lock-hc", new ThreadDeadlockHealthCheck());

        final MetricRegistry registry = new MetricRegistry();
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                .build();
        registry.gauge("thread-dead-lock-hc", () -> hcRegistry::runHealthChecks);
        reporter.start(10, TimeUnit.SECONDS);
        Thread.currentThread().join();
    }
}
