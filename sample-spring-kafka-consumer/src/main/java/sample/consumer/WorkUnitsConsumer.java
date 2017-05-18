package sample.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import sample.consumer.domain.WorkUnit;

@Service
public class WorkUnitsConsumer {
    private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @KafkaListener(topics = "workunits")
    @NewSpan
    public void onReceiving(WorkUnit workUnit, @Header(KafkaHeaders.OFFSET) Integer offset,
                            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
                            ) {
        log.info("Processing topic = {}, partition = {}, offset = {}, workUnit = {}",
                topic, partition, offset, workUnit);
    }
}
