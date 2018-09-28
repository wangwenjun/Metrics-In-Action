package com.wangwenjun.metrics.registry;

import com.codahale.metrics.MetricRegistry;

public class MetricRegistryExample
{
    public static void main(String[] args)
    {
        String name = MetricRegistry.name("Alex", "hello", "metrics");
        System.out.println(name);

        String cName = MetricRegistry.name(MetricRegistryExample.class, "hello", "metrics");
        System.out.println(cName);
    }
}
