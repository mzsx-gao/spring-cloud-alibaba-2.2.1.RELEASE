/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.nacos;

import com.alibaba.cloud.nacos.client.NacosPropertySourceLocator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaojing
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.nacos.config.enabled", matchIfMissing = true)
public class NacosConfigBootstrapConfiguration {

    //配置中心属性配置类，对应bootstrap.properties中的配置信息
	@Bean
	@ConditionalOnMissingBean
	public NacosConfigProperties nacosConfigProperties() {
		return new NacosConfigProperties();
	}

	//持有NacosConfigProperties和ConfigService
	@Bean
	@ConditionalOnMissingBean
	public NacosConfigManager nacosConfigManager(
			NacosConfigProperties nacosConfigProperties) {
		return new NacosConfigManager(nacosConfigProperties);
	}

	//加载nacos配置中心中的配置信息
	@Bean
	public NacosPropertySourceLocator nacosPropertySourceLocator(
			NacosConfigManager nacosConfigManager) {
		return new NacosPropertySourceLocator(nacosConfigManager);
	}

}
