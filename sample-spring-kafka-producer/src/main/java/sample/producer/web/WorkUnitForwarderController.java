package sample.producer.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sample.producer.domain.WorkUnit;

/**
 * Created by amitmisra on 5/22/17.
 */
@RestController
public class WorkUnitForwarderController {

    @GetMapping("/work")
    @ContinueSpan
    public boolean sendMessage(WorkUnit workUnit) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity("http://localhost:8080/generateWork", Boolean.class, workUnit);
        return Boolean.TRUE;
    }

}
