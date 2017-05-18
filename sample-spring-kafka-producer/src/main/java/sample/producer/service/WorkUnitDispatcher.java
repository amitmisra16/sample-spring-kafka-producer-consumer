package sample.producer.service;


import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import sample.producer.config.KafkaProducerProperties;
import sample.producer.domain.WorkUnit;

import java.util.HashMap;
import java.util.Map;

@Service
@SpanName(value="dispatch")
public class WorkUnitDispatcher {

    @Autowired
    private Tracer tracer;

    @Autowired
    private KafkaTemplate<String, WorkUnit> workUnitsKafkaTemplate;

    @Autowired
    private KafkaProducerProperties kafkaProducerProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkUnitDispatcher.class);

    public boolean dispatch(WorkUnit workUnit) {
        try {
            SendResult<String, WorkUnit> sendResult = workUnitsKafkaTemplate.send(new Message<WorkUnit>() {
                @Override
                public WorkUnit getPayload() {
                    return workUnit;
                }

                @Override
                public MessageHeaders getHeaders() {
                    Span span = tracer.getCurrentSpan();
                    Map<String, Object> headersMap = new HashMap<>();
                    headersMap.put(Span.TRACE_ID_NAME, new Long(span.getTraceId()).toString());
                    headersMap.put("traceId", new Long(span.getTraceId()).toString());
                    headersMap.put(Span.SPAN_ID_NAME, new Long(span.getSpanId()));
                    headersMap.put("spanId", new Long(span.getSpanId()));
                    headersMap.put("name", "Sunaina");

//                    headersMap.put("B3-Sampled", tracer.)
//                    headersMap.put("X-B3-ParentSpanId", span.getParents().get(0));
                    headersMap.put(Span.SPAN_NAME_NAME, span.getName());
                    headersMap.put(Span.SPAN_EXPORT_NAME, span.isExportable());
                    MessageHeaders messageHeaders = new MessageHeaders(headersMap);
                    return messageHeaders;
                }
            }).get();
            RecordMetadata recordMetadata = sendResult.getRecordMetadata();
            LOGGER.info("topic = {}, partition = {}, offset = {}, workUnit = {}",
                    recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), workUnit);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
