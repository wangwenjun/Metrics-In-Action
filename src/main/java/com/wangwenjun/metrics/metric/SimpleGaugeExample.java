package com.wangwenjun.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class SimpleGaugeExample
{
    private static final MetricRegistry metricRegistry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    private static final BlockingDeque<Long> queue = new LinkedBlockingDeque<>(1_000);

    public static void main(String[] args)
    {

        metricRegistry.register(MetricRegistry.name(SimpleGaugeExample.class, "queue-size"), (Gauge<Integer>) queue::size);

        reporter.start(1, TimeUnit.SECONDS);

        new Thread(() ->
        {
            for (; ; )
            {
                randomSleep();
                queue.add(System.nanoTime());
            }
        }).start();

        new Thread(() ->
        {
            for (; ; )
            {
                randomSleep();
                queue.poll();
            }
        }).start();
    }

    private static void randomSleep()
    {
        try
        {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(6));
        } catch (InterruptedException e)
        {
        }
    }
}
