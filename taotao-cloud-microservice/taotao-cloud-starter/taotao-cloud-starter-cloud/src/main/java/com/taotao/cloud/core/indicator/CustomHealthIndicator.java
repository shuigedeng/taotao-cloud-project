/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.core.indicator;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * CustomHealthIndicator
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/04/02 10:23
 */
public class CustomHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		List<GarbageCollectorMXBean> gge = ManagementFactory.getGarbageCollectorMXBeans();
		Health.Builder builder = new Health.Builder();
		builder.status("run");
		builder.withDetail("garbage", gge);
		return builder.build();
	}

}
