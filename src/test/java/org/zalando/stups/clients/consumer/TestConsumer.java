/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
