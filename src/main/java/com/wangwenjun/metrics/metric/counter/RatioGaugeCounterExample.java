package com.wangwenjun.metrics.metric.counter;

import com.codahale.metrics.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class RatioGaugeCounterExample
{
    private final static MetricRegistry register = new MetricRegistry();
    private final static ConsoleReporter reporter = ConsoleReporter.forRegistry(register)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    private final static Counter totalCounter = new Counter();
    private final static Counter successCounter = new Counter();

    public static void main(String[] args)
    {
        reporter.start(10, TimeUnit.SECONDS);
        register.gauge("success-rate", () -> new RatioGauge()
        {
            @Override
            protected Ratio getRatio()
            {
                return Ratio.of(successCounter.getCount(), totalCounter.getCount());
            }
        });

        for (; ; )
        {
            shortSleep();
            business();
        }
    }

    private static void business()
    {
        //total inc
        totalCounter.inc();
        try
        {
            int x = 10 / ThreadLocalRandom.current().nextInt(6);
            successCounter.inc();
            //success inc
        } catch (Exception e)
        {
            System.out.println("ERROR");
        }
    }

    private static void shortSleep()
    {
        try
        {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(6));
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
