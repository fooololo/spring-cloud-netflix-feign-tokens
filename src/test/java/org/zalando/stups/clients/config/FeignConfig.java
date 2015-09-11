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
package org.zalando.stups.clients.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;

import org.springframework.context.annotation.Configuration;

import org.zalando.stups.spring.cloud.netflix.feign.EnableOAuth2FeignClients;

/**
 * Instead of using {@link EnableFeignClients} we use {@link EnableOAuth2FeignClients} here.<br/>
 * Maybe you put non-oauth feign-clients into different packages.
 *
 * @author  jbellmann
 */
@Configuration
@EnableOAuth2FeignClients(basePackages = {"org.zalando.stups.clients.feign"})
public class FeignConfig { }
