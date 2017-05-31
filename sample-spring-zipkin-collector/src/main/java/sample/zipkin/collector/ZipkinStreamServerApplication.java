package sample.zipkin.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

/**
 * Created by amitmisra on 5/22/17.
 */
@SpringBootApplication
@EnableZipkinStreamServer
public class ZipkinStreamServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinStreamServerApplication.class, args);
    }

}
