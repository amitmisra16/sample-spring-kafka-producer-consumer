package sample.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sample.consumer.domain.WorkUnit;

@Service
public class WorkUnitsConsumer {
    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @KafkaListener(topics = "workunitsDemo8")
    @ContinueSpan(log = "Received work unit")
    public void onReceiving(WorkUnit workUnit) {
        log.info("Processing workUnit = {}", workUnit);
    }
}
