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

    @Around("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public Object traceAspect(ProceedingJoinPoint aPjp) throws Throwable {
        WorkUnit workUnit = (WorkUnit) aPjp.getArgs()[0];
        Span span = Span.builder().traceId(Span.hexToId(workUnit.getId())).spanId(Span.hexToId(workUnit.getId())).build();
        tracer.continueSpan(span);
        Object result = aPjp.proceed(aPjp.getArgs());
        tracer.close(span);
        return result;
    }
}
