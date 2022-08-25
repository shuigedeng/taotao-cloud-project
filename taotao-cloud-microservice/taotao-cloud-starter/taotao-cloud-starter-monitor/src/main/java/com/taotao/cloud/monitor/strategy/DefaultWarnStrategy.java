/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.monitor.strategy;

import com.taotao.cloud.common.utils.common.PropertyUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.monitor.collect.IOCollectTask;
import com.taotao.cloud.monitor.model.Report;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DefaultWarnStrategy
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:02:08
 */
public class DefaultWarnStrategy implements WarnStrategy{

	protected static int maxCacheSize = 3;
	protected static List<Report> cacheReports = Collections.synchronizedList(new ArrayList<>(maxCacheSize + 2));
	protected Rule.RulesAnalyzer rulesAnalyzer;
	protected WarnTemplate warnTemplate;

	public Rule.RulesAnalyzer getRulesAnalyzer() {
		return rulesAnalyzer;
	}

	public WarnTemplate getWarnTemplate() {
		return warnTemplate;
	}

	public DefaultWarnStrategy(WarnTemplate warnTemplate, Rule.RulesAnalyzer rulesAnalyzer) {
		this.warnTemplate = warnTemplate;
		this.rulesAnalyzer = rulesAnalyzer;

		setDefaultStrategy();
	}

	/**
	 * setDefaultStrategy
	 *
	 * @author shuigedeng
	 * @since 2021-09-10 17:02:40
	 */
	public void setDefaultStrategy() {
		rulesAnalyzer.registerRules("cpu.process",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.cpu.process", "[>0.7]"));
		rulesAnalyzer.registerRules("cpu.system",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.cpu.system", "[>0.7]"));
		rulesAnalyzer.registerRules("io.current.dir.usable.size",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.io.current.dir.usable.size",
				"[<500]"));
		rulesAnalyzer.registerRules("memery.jvm.max",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.memery.jvm.max", "[<256]"));
		rulesAnalyzer.registerRules("memery.system.free",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.memery.system.free",
				"[<256]"));
		rulesAnalyzer.registerRules("thread.deadlocked.count",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.thread.deadlocked.count",
				"[>10]"));
		rulesAnalyzer.registerRules("thread.total",
			PropertyUtil.getPropertyCache("taotao.cloud.health.strategy.thread.total", "[>1000]"));
		rulesAnalyzer.registerRules("tomcat.threadPool.poolSize.count",
			PropertyUtil.getPropertyCache(
				"taotao.cloud.health.strategy.tomcat.threadPool.poolSize.count",
				"[>1000]"));
		rulesAnalyzer.registerRules("tomcat.threadPool.active.count",
			PropertyUtil.getPropertyCache(
				"taotao.cloud.health.strategy.tomcat.threadPool.active.count",
				"[>200]"));
		rulesAnalyzer.registerRules("tomcat.threadPool.queue.size",
			PropertyUtil.getPropertyCache(
				"taotao.cloud.health.strategy.tomcat.threadPool.queue.size",
				"[>50]"));

		if (rulesAnalyzer.getRules("io.current.dir.usable.size") != null) {
			//设置报警回调
			rulesAnalyzer.getRules("io.current.dir.usable.size").forEach(c -> {
				if (c.getType() == Rule.RuleType.less) {
					c.setHitCallBack((value) -> IOCollectTask.clearLog());
				}
			});
		}
	}

	/**
	 * analyse
	 *
	 * @param report report
	 * @return {@link Report }
	 * @author shuigedeng
	 * @since 2021-09-10 17:02:44
	 */
	@Override
	public Report analyse(Report report) {
		while (cacheReports.size() > maxCacheSize) {
			cacheReports.remove(0);
		}

		cacheReports.add(report);
		Report avgReport = report.avgReport(cacheReports);
		return rulesAnalyzer.analyse(avgReport);
	}

	/**
	 * analyseText
	 *
	 * @param report report
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-10 17:02:49
	 */
	@Override
	public String analyseText(Report report) {
		Report r = analyse(report);
		StringBuilder warn = new StringBuilder();

		r.eachReport((filed, item) -> {
			if (item.isWarn()) {
				warn.append(warnTemplate.getWarnContent(filed, item.getDesc(), item.getValue(),
					item.getRule()));
			}
			return item;
		});

		String warnInfo = warn.toString();
		if (StringUtil.isEmpty(warnInfo)) {
			return "";
		}
		return warnInfo;
	}

}
