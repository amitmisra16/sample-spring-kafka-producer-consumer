package sample.consumer.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import sample.consumer.domain.WorkUnit;

/**
 * Created by amitmisra on 5/18/17.
 */
@Aspect
@Component
public class TraceAspect {

    private static final Logger logger = LoggerFactory.getLogger(TraceAspect.class);

    @Autowired
    private Tracer tracer;

    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener) && args(workUnit)")
    public Object traceAspect(ProceedingJoinPoint aPjp, WorkUnit workUnit) throws Throwable {
        Span span = tracer.createSpan("receiver work unit", Span.builder()
                .traceId(Span.hexToId(workUnit.getId()))
                .spanId(Span.hexToId(workUnit.getId()))
                .name(String.format("SpanName%s",workUnit.getId()))
                .baggage("workUnitId", workUnit.getId())
                .build());
        span.logEvent("Received work unit");
        Object result = aPjp.proceed(aPjp.getArgs());
        span.logEvent("Processed work unit");
        tracer.close(span);
        return result;
    }
}
