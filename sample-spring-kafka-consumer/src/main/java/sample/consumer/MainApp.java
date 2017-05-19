package sample.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import sample.consumer.config.KafkaConsumerProperties;

@SpringBootApplication
@ComponentScan("sample.consumer")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties(KafkaConsumerProperties.class)
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}
