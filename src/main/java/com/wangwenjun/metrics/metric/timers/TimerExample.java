package com.wangwenjun.metrics.metric.timers;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class TimerExample
{
    private final static MetricRegistry registry = new MetricRegistry();
    private final static ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();

    private final static Timer timer = registry.timer("request", Timer::new);

    public static void main(String[] args)
    {
        reporter.start(10, TimeUnit.SECONDS);
        while (true)
        {
            business();
        }
    }

    private static void business()
    {
        Timer.Context context = timer.time();
        try
        {
            TimeUnit.SECONDS.sleep(current().nextInt(10));
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } finally
        {
            long stop = context.stop();
            System.out.println("=====" + stop);
        }
    }
}
