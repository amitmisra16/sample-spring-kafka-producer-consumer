package sample.consumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSpanTextMapInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import sample.consumer.domain.WorkUnit;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaListenerConfig {

    @Autowired
    private KafkaConsumerProperties kafkaConsumerProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WorkUnit> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WorkUnit> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, WorkUnit> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps(), stringKeyDeserializer(), workUnitJsonValueDeserializer());
    }


    @Bean
    public Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.getBootstrap());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getGroup());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean
    public Deserializer stringKeyDeserializer() {
        return new StringDeserializer();
    }

    @Bean
    public Deserializer workUnitJsonValueDeserializer() {
        return new JsonDeserializer(WorkUnit.class);
    }


    @Bean
    public MessagingSpanTextMapInjector messagingSpanTextMapInjector() {
        return new MessagingSpanTextMapInjector() {
            @Override
            public void inject(Span span, SpanTextMap carrier) {
                carrier.put(Span.TRACE_ID_NAME, span.traceIdString());
                carrier.put(Span.SPAN_ID_NAME, Span.idToHex(span.getSpanId()));
            }
        };
    }
}
