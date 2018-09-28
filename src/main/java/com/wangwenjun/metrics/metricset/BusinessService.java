package com.wangwenjun.metrics.metricset;

import com.codahale.metrics.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

public class BusinessService extends Thread implements MetricSet
{
    private final Map<String, Metric> metrics = new HashMap<>();

    private final Counter totalBusiness = new Counter();
    private final Counter successBusiness = new Counter();
    private final Counter failBusiness = new Counter();
    private final Timer timer = new Timer();
    private final Histogram volumeHisto = new Histogram(new ExponentiallyDecayingReservoir());
    private final RatioGauge successGauge = new RatioGauge()
    {
        @Override
        protected Ratio getRatio()
        {
            return Ratio.of(successBusiness.getCount(), totalBusiness.getCount());
        }
    };

    public BusinessService()
    {
        metrics.put("cloud-disk-upload-total", totalBusiness);
        metrics.put("cloud-disk-upload-success", successBusiness);
        metrics.put("cloud-disk-upload-failure", failBusiness);
        metrics.put("cloud-disk-upload-frequency", timer);
        metrics.put("cloud-disk-upload-volume", volumeHisto);
        metrics.put("cloud-disk-upload-suc-rate", successGauge);
    }

    @Override
    public void run()
    {
        while (true)
        {
            upload(new byte[current().nextInt(10_000)]);
        }
    }

    private void upload(byte[] buffer)
    {
        totalBusiness.inc();
        Timer.Context context = timer.time();
        try
        {
            int x = 1 / current().nextInt(10);
            TimeUnit.MILLISECONDS.sleep(200);
            volumeHisto.update(buffer.length);
            successBusiness.inc();
        } catch (Exception e)
        {
            failBusiness.inc();
        } finally
        {
            context.close();
        }

    }

    @Override
    public Map<String, Metric> getMetrics()
    {
        return metrics;
    }
}
