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

import org.zalando.stups.tokens.AccessTokens;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author  jbellmann
 */
class AccessTokensRequestInterceptor implements RequestInterceptor {

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final AccessTokens accessTokens;
    private final String serviceName;

    AccessTokensRequestInterceptor(final AccessTokens accessTokens, final String serviceName) {
        this.accessTokens = accessTokens;
        this.serviceName = serviceName;
    }

    @Override
    public void apply(final RequestTemplate template) {
        template.header(AUTHORIZATION, BEARER + getAccessToken());
    }

    private String getAccessToken() {
        return accessTokens.get(serviceName);
    }
}
