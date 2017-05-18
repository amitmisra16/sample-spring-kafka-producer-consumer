package sample.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by amitmisra on 5/17/17.
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @RequestMapping("/test")
    public void test() {
        logger.info("Hello World!!!");
    }
}
