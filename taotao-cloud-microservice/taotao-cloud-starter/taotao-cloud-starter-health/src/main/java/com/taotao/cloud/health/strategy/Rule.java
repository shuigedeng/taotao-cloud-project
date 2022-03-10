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
package com.taotao.cloud.health.strategy;

import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.common.model.PropertyCache;
import com.taotao.cloud.common.utils.common.PropertyUtil;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.health.model.Report;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.util.StringUtils;

/**
 * 规则引擎
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 16:54:40
 */
public class Rule {

	/**
	 * 规则
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-10 16:54:58
	 */
	public static class RuleInfo implements Serializable {

		private RuleType type;
		private Object value;
		private HitCallBack hitCallBack;

		public RuleInfo() {
		}

		public RuleInfo(RuleType type, Object value,
			HitCallBack hitCallBack) {
			this.type = type;
			this.value = value;
			this.hitCallBack = hitCallBack;
		}

		/**
		 * check
		 *
		 * @param checkValue checkValue
		 * @return boolean
		 * @author shuigedeng
		 * @since 2021-09-10 16:55:04
		 */
		public boolean check(Object checkValue) {
			if (checkValue == null) {
				return false;
			}

			try {
				if (checkValue instanceof Number) {
					double checkValue2 = ((Number) checkValue).doubleValue();
					double warnValue = (BeanUtil.convert(value, Number.class)).doubleValue();

					if (type == RuleType.less && checkValue2 < warnValue) {
						return true;
					} else if (type == RuleType.more && checkValue2 > warnValue) {
						return true;
					} else if (type == RuleType.equal && checkValue2 == warnValue) {
						return true;
					}
				} else {
					String checkValue2 = checkValue.toString();
					String warnValue = value.toString();
					if (type == RuleType.equal && Objects.equals(checkValue2, warnValue)) {
						return true;
					} else if (type == RuleType.contain && checkValue2.contains(warnValue)) {
						return true;
					}
				}
			} catch (Exception exp) {
				LogUtil.error("health", "check 规则检查出错", exp);
			}
			return false;
		}

		public RuleType getType() {
			return type;
		}

		public void setType(RuleType type) {
			this.type = type;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public HitCallBack getHitCallBack() {
			return hitCallBack;
		}

		public void setHitCallBack(HitCallBack hitCallBack) {
			this.hitCallBack = hitCallBack;
		}
	}

	/**
	 * 规则分析器
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-10 16:55:37
	 */
	static public class RulesAnalyzer {

		private final Map<String, List<RuleInfo>> rules = new HashMap<>();
		private final RuleParser ruleParser = new RuleParser();

		public RulesAnalyzer() {
			PropertyCache propertyCache = ContextUtil.getBean(PropertyCache.class, false);
			if (Objects.nonNull(propertyCache)) {
				//订阅配置改变，重新注册规则
				propertyCache.listenUpdateCache("RulesAnalyzer 动态规则订阅", (map) -> {
					for (Map.Entry<String, Object> e : map.entrySet()) {
						String key = e.getKey();

						if (StringUtils.startsWithIgnoreCase(key,
							"taotao.cloud.health.strategy.")) {
							key = key.replace("taotao.cloud.health.strategy.", "");
							Object rule = rules.get(key);
							if (rule != null) {
								registerRules(key, StringUtil.nullToEmpty(e.getValue()));
							}
						}
					}
				});
			}
		}

		/**
		 * parserRules
		 *
		 * @param rules rules
		 * @return {@link java.util.List }
		 * @author shuigedeng
		 * @since 2021-09-10 16:55:42
		 */
		public List<RuleInfo> parserRules(String rules) {
			return ruleParser.parser(rules);
		}

		/**
		 * getRules
		 *
		 * @param field field
		 * @return {@link java.util.List }
		 * @author shuigedeng
		 * @since 2021-09-10 16:55:45
		 */
		public List<RuleInfo> getRules(String field) {
			List<RuleInfo> item = rules.get(field);
			if (item == null) {
				registerRulesByProperties(field);
			}
			return rules.get(field);
		}

		/**
		 * registerRules
		 *
		 * @param field field
		 * @param rules rules
		 * @author shuigedeng
		 * @since 2021-09-10 16:55:48
		 */
		public void registerRules(String field, List<RuleInfo> rules) {
			//if(this.rules.containsKey(field)) {
			//    this.rules.get(field).addAll(rules);
			//
			//}else{
			//
			//}
			this.rules.put(field, rules);
		}

		/**
		 * registerRules
		 *
		 * @param field field
		 * @param rules rules
		 * @author shuigedeng
		 * @since 2021-09-10 16:55:50
		 */
		public void registerRules(String field, String rules) {
			registerRules(field, ruleParser.parser(rules));
		}

		/**
		 * registerRulesByProperties
		 *
		 * @param field field
		 * @author shuigedeng
		 * @since 2021-09-10 16:55:53
		 */
		public void registerRulesByProperties(String field) {
			String value = PropertyUtil.getPropertyCache("taotao.cloud.health.strategy." + field,
				"");
			registerRules(field, value);
		}

		/**
		 * analyse
		 *
		 * @param report report
		 * @return {@link com.taotao.cloud.health.model.Report }
		 * @author shuigedeng
		 * @since 2021-09-10 16:55:56
		 */
		public Report analyse(Report report) {
			report.eachReport((fieldName, item) -> {
				List<RuleInfo> rules = this.getRules(fieldName);
				if (rules != null && !rules.isEmpty()) {
					for (RuleInfo ruleInfo : rules) {
						boolean isWarn = ruleInfo.check(item.getValue());

						if (isWarn) {
							item.setWarn("报警");
							item.setRule(ruleInfo);
							if (ruleInfo.getHitCallBack() != null) {
								try {
									ruleInfo.hitCallBack.run(item.getValue());
								} catch (Exception exp) {
									LogUtil.error("health",
										"analyse分析时执行报警回调规则出错", exp);
								}
							}
						}
					}
				}
				return item;
			});
			return report;
		}
	}

	/**
	 * RuleType
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-10 16:56:05
	 */
	public static enum RuleType {
		more(">", "大于"),
		less("<", "小于"),
		equal("=", "等于"),
		contain("%", "包含");

		private final String desc;
		private final String tag;

		RuleType(String tag, String desc) {
			this.desc = desc;
			this.tag = tag;
		}

		public static RuleType getRuleType(String tag) {
			for (RuleType type : RuleType.values()) {
				if (type.tag.equalsIgnoreCase(tag)) {
					return type;
				}
			}
			return null;
		}
	}

	/**
	 * 规则解析器
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-10 16:56:13
	 */
	public static class RuleParser {

		/**
		 * parser
		 *
		 * @param text text
		 * @return {@link java.util.List }
		 * @author shuigedeng
		 * @since 2021-09-10 16:56:27
		 */
		public List<RuleInfo> parser(String text) {
			List<RuleInfo> result = new ArrayList<>();
			try {
				if (StringUtil.isNotBlank(text)) {
					if (text.startsWith("[") && text.endsWith("]")) {
						text = text
							.replace("[", "")
							.replace("]", "");

						String[] rules = text.split(";");
						for (String r : rules) {
							RuleType type = RuleType.getRuleType(r.charAt(0) + "");
							String value = StringUtils.trimLeadingCharacter(r, r.charAt(0));
							//val numvalue = ConvertUtils.tryConvert(value, Number.class);
							if (type != null) {
								result.add(new RuleInfo(type, value, null));
							}
						}
					}
				}
			} catch (Exception exp) {
				LogUtil.error("health", "parser规则解析出错", exp);
			}
			return result;
		}
	}

	/**
	 * 命中回调
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-10 16:56:20
	 */
	public interface HitCallBack {

		/**
		 * run
		 *
		 * @param value value
		 * @author shuigedeng
		 * @since 2021-09-10 16:56:24
		 */
		void run(Object value);
	}
}
