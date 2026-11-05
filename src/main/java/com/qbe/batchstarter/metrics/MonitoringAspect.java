package com.qbe.batchstarter.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MonitoringAspect {

    private final MeterRegistry meterRegistry;

    @Around("@annotation(monitored)")
    public Object monitor(ProceedingJoinPoint joinPoint, Monitored monitored) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            Object result = joinPoint.proceed();
            meterRegistry.counter(monitored.name() + ".success").increment();
            return result;
        } catch (Exception e) {
            meterRegistry.counter(monitored.name() + ".error").increment();
            throw e;
        } finally {
            sample.stop(Timer.builder(monitored.name() + ".duration").register(meterRegistry));
        }
    }
}
