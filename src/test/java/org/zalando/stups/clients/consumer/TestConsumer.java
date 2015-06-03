package org.zalando.stups.clients.consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.zalando.stups.clients.feign.KioFeignClient;

/**
 * This consumer is just autowiring the created feign-client to see that creation of feign-client works.
 *
 * @author  jbellmann
 */
@Component
public class TestConsumer {

    @Autowired
    private KioFeignClient kioFeignClient;

    @PostConstruct
    public void init() {
        Assert.notNull(kioFeignClient, "KioFeignClient should not be null");
    }

    public void useKioFeignClient() {
        // first we need test-support for that
        // kioFeignClient.getSomewhat("test");
    }
}
