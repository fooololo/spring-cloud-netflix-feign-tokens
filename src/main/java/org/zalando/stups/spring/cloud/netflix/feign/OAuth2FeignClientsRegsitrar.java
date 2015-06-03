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
package org.zalando.stups.spring.cloud.netflix.feign;

import java.lang.annotation.Annotation;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.FeignClientsRegistrar;
import org.springframework.cloud.netflix.feign.TokensFeignClientFactoryBean;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Same as {@link FeignClientsRegistrar} the only change needed was to register another type of FeinClientFactoryBean.
 * Here we use {@link TokensFeignClientFactoryBean}. Maybe we can configure this in the 'enable'-annotation somehow.
 *
 * @author  jbellmann
 */
class OAuth2FeignClientsRegsitrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware {

    // patterned after Spring Integration IntegrationComponentScanRegistrar

    private ResourceLoader resourceLoader;

    private ClassLoader classLoader;

    public OAuth2FeignClientsRegsitrar() { }

    @Override
    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void registerBeanDefinitions(final AnnotationMetadata importingClassMetadata,
            final BeanDefinitionRegistry registry) {

        Set<String> basePackages = getBasePackages(importingClassMetadata);

        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.addIncludeFilter(new AnnotationTypeFilter(FeignClient.class));
        scanner.setResourceLoader(this.resourceLoader);

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {

                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                    Assert.isTrue(annotationMetadata.isInterface(),
                        "@FeignClient can only be specified on an interface");

                    BeanDefinitionHolder holder = createBeanDefinition(annotationMetadata);
                    BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
                }
            }
        }
    }

    private BeanDefinitionHolder createBeanDefinition(final AnnotationMetadata annotationMetadata) {
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(FeignClient.class
                    .getCanonicalName());

        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(
                TokensFeignClientFactoryBean.class);

        validate(attributes);
        definition.addPropertyValue("url", getUrl(attributes));
        definition.addPropertyValue("name", getServiceId(attributes));
        definition.addPropertyValue("type", className);

        String beanName = StringUtils.uncapitalize(className.substring(className.lastIndexOf(".") + 1));
        return new BeanDefinitionHolder(definition.getBeanDefinition(), beanName);
    }

    private void validate(final Map<String, Object> attributes) {
        if (StringUtils.hasText((String) attributes.get("value"))) {
            Assert.isTrue(!StringUtils.hasText((String) attributes.get("name")),
                "Either name or value can be specified, but not both");
        }
    }

    private String getServiceId(final Map<String, Object> attributes) {
        String name = (String) attributes.get("name");
        if (!StringUtils.hasText(name)) {
            name = (String) attributes.get("value");
        }

        if (!StringUtils.hasText(name)) {
            return "";
        }

        String host = null;
        try {
            host = new URI("http://" + name).getHost();
        } catch (URISyntaxException e) { }

        Assert.state(host != null, "Service id not legal hostname (" + name + ")");
        return name;
    }

    private String getUrl(final Map<String, Object> attributes) {
        return (String) attributes.get("url");
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false) {

            @Override
            protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {

                    // TODO until SPR-11711 will be resolved
                    if (beanDefinition.getMetadata().isInterface()
                            && beanDefinition.getMetadata().getInterfaceNames().length == 1
                            && Annotation.class.getName().equals(beanDefinition.getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(),
                                    OAuth2FeignClientsRegsitrar.this.classLoader);
                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            this.logger.error("Could not load target class: "
                                    + beanDefinition.getMetadata().getClassName(), ex);

                        }
                    }

                    return true;
                }

                return false;

            }
        };
    }

    protected Set<String> getBasePackages(final AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableOAuth2FeignClients.class
                    .getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return basePackages;
    }

}
