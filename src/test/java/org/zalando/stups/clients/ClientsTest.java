package org.zalando.stups.clients;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zalando.stups.clients.consumer.TestConsumer;
import org.zalando.stups.clients.feign.KioFeignClient;

/**
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SampleApplication.class})
@WebIntegrationTest(randomPort = false)
@ActiveProfiles("custom")
public class ClientsTest {

    @Autowired
    private KioFeignClient kioFeignClient;

    @Autowired
    private TestConsumer testConsumer;

    @Test
    public void testKioClient() {

        // testConsumer.useKioFeignClient();

    }

}
