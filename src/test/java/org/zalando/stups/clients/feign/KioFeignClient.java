package org.zalando.stups.clients.feign;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author  jbellmann
 */
@FeignClient("kio")
public interface KioFeignClient {

    @RequestMapping(value = "/somewhere/{term}", method = RequestMethod.GET)
    Map<String, String> getSomewhat(String term);
}
