package com.wangwenjun.metrics.metric.gauge;

import com.codahale.metrics.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class DerivativeGaugeExample
{
    private final static LoadingCache<String, String> cache = CacheBuilder
            .newBuilder().maximumSize(10)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .recordStats()
            .build(new CacheLoader<String, String>()
            {
                @Override
                public String load(String key) throws Exception
                {
                    return key.toUpperCase();
                }
            });

    private final static MetricRegistry registry = new MetricRegistry();
    private final static ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws InterruptedException
    {
        reporter.start(10, TimeUnit.SECONDS);
        Gauge<CacheStats> cacheGauge = registry.gauge("cache-stats", () -> cache::stats);
        registry.register("missCount", new DerivativeGauge<CacheStats, Long>(cacheGauge)
        {
            @Override
            protected Long transform(CacheStats stats)
            {
                return stats.missCount();
            }
        });

        registry.register("loadExceptionCount", new DerivativeGauge<CacheStats, Long>(cacheGauge)
        {
            @Override
            protected Long transform(CacheStats stats)
            {
                return stats.loadExceptionCount();
            }
        });

        while (true)
        {
            business();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void business()
    {
        cache.getUnchecked("alex");
    }
}
