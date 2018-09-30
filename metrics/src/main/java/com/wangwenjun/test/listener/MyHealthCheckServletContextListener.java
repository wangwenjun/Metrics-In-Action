package com.wangwenjun.test.listener;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.codahale.metrics.servlets.HealthCheckServlet;

public class MyHealthCheckServletContextListener
  extends HealthCheckServlet.ContextListener {

    public static HealthCheckRegistry HEALTH_CHECK_REGISTRY
      = new HealthCheckRegistry();

    static {
        HEALTH_CHECK_REGISTRY.register("ThreadDeadlock", new ThreadDeadlockHealthCheck());
    }

    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return HEALTH_CHECK_REGISTRY;
    }
}