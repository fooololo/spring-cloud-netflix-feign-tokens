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
