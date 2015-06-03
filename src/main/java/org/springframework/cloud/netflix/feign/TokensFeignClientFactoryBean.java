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
package org.springframework.cloud.netflix.feign;

import static java.util.Collections.singleton;

import org.springframework.beans.factory.annotation.Autowired;

import org.zalando.stups.tokens.AccessTokens;
import org.zalando.stups.tokens.AccessTokensBean;
import org.zalando.stups.tokens.config.AccessTokensBeanProperties;

import feign.Feign;
import feign.RequestInterceptor;

/**
 * {@link FeignClientFactoryBean} is package-protected. That makes it necessary to name this package the same. Maybe
 * this will change in the future.
 *
 * @author  jbellmann
 */
public class TokensFeignClientFactoryBean extends FeignClientFactoryBean {

    @Autowired
    private AccessTokensBean accessTokensBean;

    @Autowired
    private AccessTokensBeanProperties accessTokensBeanProperties;

    private String serviceId;

    @Override
    protected Feign.Builder feign() {

        Feign.Builder builder = super.feign();

        RequestInterceptor interceptor = buildRequestInterceptor();

        // caution, this will override all registered interceptors before
        builder.requestInterceptors(singleton(interceptor));

        return builder;
    }

    protected RequestInterceptor buildRequestInterceptor() {
        return new AccessTokensRequestInterceptor((AccessTokens) accessTokensBean, serviceId);
    }

    // to keep the name without 'http' in front of it
    // when calling super#getName() in this#feign() it is prefixed
    @Override
    public void setName(final String name) {
        this.serviceId = name;
        super.setName(name);
    }

}
