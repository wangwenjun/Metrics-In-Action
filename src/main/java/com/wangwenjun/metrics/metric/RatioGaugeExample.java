package com.wangwenjun.metrics.metric;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/***************************************
 * @author:Alex Wang <br/>
 * @taobao:http://wangwenjun0609.taobao.com
 ***************************************/
public class RatioGaugeExample
{
    private final static MetricRegistry register = new MetricRegistry();
    private final static ConsoleReporter reporter = ConsoleReporter.forRegistry(register)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.SECONDS)
            .build();
    private final static Meter totalMeter = new Meter();
    private final static Meter successMeter = new Meter();

    public static void main(String[] args)
    {
        reporter.start(10, TimeUnit.SECONDS);
        register.gauge("success-rate", () -> new RatioGauge()
        {
            @Override
            protected Ratio getRatio()
            {
                return Ratio.of(successMeter.getCount(), totalMeter.getCount());
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
        totalMeter.mark();
        try
        {
            int x = 10 / ThreadLocalRandom.current().nextInt(6);
            successMeter.mark();
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
