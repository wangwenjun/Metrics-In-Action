package com.wangwenjun.metrics.metric.gauge;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class CachedGaugeExample
{
    private final static MetricRegistry registry = new MetricRegistry();
    private final static ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws InterruptedException
    {
        reporter.start(10, TimeUnit.SECONDS);
        registry.gauge("cached-db-size", () -> new CachedGauge<Long>(30, TimeUnit.SECONDS)
        {
            @Override
            protected Long loadValue()
            {
                return queryFromDB();
            }
        });

        System.out.println("============" + new Date());
        Thread.currentThread().join();
    }

    private static long queryFromDB()
    {
        System.out.println("====queryFromDB=====");
        return System.currentTimeMillis();
    }
}