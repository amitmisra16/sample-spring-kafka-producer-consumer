package sample.producer.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.producer.domain.WorkUnit;
import sample.producer.service.WorkUnitDispatcher;

@RestController
public class SampleKafkaMessageController {

    @Autowired
    private WorkUnitDispatcher workUnitDispatcher;

    @GetMapping("/generateWork")
    @ContinueSpan
    public boolean sendMessage(WorkUnit workUnit) {
        return this.workUnitDispatcher.dispatch(workUnit);
    }
}
