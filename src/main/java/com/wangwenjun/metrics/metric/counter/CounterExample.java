package com.wangwenjun.metrics.metric.counter;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class CounterExample
{
    private static final MetricRegistry metricRegistry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    private static final BlockingDeque<Long> queue = new LinkedBlockingDeque<>(1_000);

    public static void main(String[] args)
    {
        reporter.start(10, TimeUnit.SECONDS);
        Counter counter = metricRegistry.counter("queue-count", Counter::new);

        new Thread(() ->
        {
            for (; ; )
            {
                randomSleep();
                queue.add(System.nanoTime());
                counter.inc();
            }
        }).start();

        new Thread(() ->
        {
            for (; ; )
            {
                randomSleep();
                if (queue.poll() != null)
                    counter.dec();
            }
        }).start();
    }

    private static void randomSleep()
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(500));
        } catch (InterruptedException e)
        {
        }
    }
}
