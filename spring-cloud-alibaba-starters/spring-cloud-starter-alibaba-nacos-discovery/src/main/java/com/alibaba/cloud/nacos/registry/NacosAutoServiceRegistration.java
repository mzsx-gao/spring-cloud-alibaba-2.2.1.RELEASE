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

package com.alibaba.cloud.nacos.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 自动注册功能，这个类继承AbstractAutoServiceRegistration，从而能够监听到WebServerInitializedEvent事件，
 * 继而调用NacosRegistration中的register()方法完成注册
 */
public class NacosAutoServiceRegistration
		extends AbstractAutoServiceRegistration<Registration> {

	private static final Logger log = LoggerFactory
			.getLogger(NacosAutoServiceRegistration.class);

	private NacosRegistration registration;

	public NacosAutoServiceRegistration(ServiceRegistry<Registration> serviceRegistry,
			AutoServiceRegistrationProperties autoServiceRegistrationProperties,
			NacosRegistration registration) {
		super(serviceRegistry, autoServiceRegistrationProperties);
		this.registration = registration;
	}

	@Deprecated
	public void setPort(int port) {
		getPort().set(port);
	}

	@Override
	protected NacosRegistration getRegistration() {
		if (this.registration.getPort() < 0 && this.getPort().get() > 0) {
			this.registration.setPort(this.getPort().get());
		}
		Assert.isTrue(this.registration.getPort() > 0, "service.port has not been set");
		return this.registration;
	}

	@Override
	protected NacosRegistration getManagementRegistration() {
		return null;
	}

	//自动注册服务
	@Override
	protected void register() {
		if (!this.registration.getNacosDiscoveryProperties().isRegisterEnabled()) {
			log.debug("Registration disabled.");
			return;
		}
		if (this.registration.getPort() < 0) {
			this.registration.setPort(getPort().get());
		}
		super.register();
	}

	@Override
	protected void registerManagement() {
		if (!this.registration.getNacosDiscoveryProperties().isRegisterEnabled()) {
			return;
		}
		super.registerManagement();

	}

	@Override
	protected Object getConfiguration() {
		return this.registration.getNacosDiscoveryProperties();
	}

	@Override
	protected boolean isEnabled() {
		return this.registration.getNacosDiscoveryProperties().isRegisterEnabled();
	}

	@Override
	@SuppressWarnings("deprecation")
	protected String getAppName() {
		String appName = registration.getNacosDiscoveryProperties().getService();
		return StringUtils.isEmpty(appName) ? super.getAppName() : appName;
	}

}
