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
package com.taotao.cloud.health.strategy;

import com.taotao.cloud.common.model.PropertyCache;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.common.utils.common.PropertyUtil;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.health.model.Report;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 规则引擎
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:25:43
 */
public class Rule {

	/**
	 * 规则
	 *
	 * @author shuigedeng
	 * @version 2022.04
	 * @since 2022-04-27 17:25:43
	 */
	public static class RuleInfo implements Serializable {

		/**
		 * 类型
		 */
		private RuleType type;
		/**
		 * 价值
		 */
		private Object value;
		/**
		 * 打回电话
		 */
		private HitCallBack hitCallBack;

		/**
		 * 规则信息
		 *
		 * @return
		 * @since 2022-04-27 17:25:43
		 */
		public RuleInfo() {
		}

		/**
		 * 规则信息
		 *
		 * @param type        类型
		 * @param value       价值
		 * @param hitCallBack 打回电话
		 * @return
		 * @since 2022-04-27 17:25:43
		 */
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
		 * @since 2022-04-27 17:25:43
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

		/**
		 * 得到类型
		 *
		 * @return {@link RuleType }
		 * @since 2022-04-27 17:25:43
		 */
		public RuleType getType() {
			return type;
		}

		/**
		 * 集类型
		 *
		 * @param type 类型
		 * @since 2022-04-27 17:25:43
		 */
		public void setType(RuleType type) {
			this.type = type;
		}

		/**
		 * 获得价值
		 *
		 * @return {@link Object }
		 * @since 2022-04-27 17:25:43
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * 设置值
		 *
		 * @param value 价值
		 * @since 2022-04-27 17:25:43
		 */
		public void setValue(Object value) {
			this.value = value;
		}

		/**
		 * 会打回电话
		 *
		 * @return {@link HitCallBack }
		 * @since 2022-04-27 17:25:43
		 */
		public HitCallBack getHitCallBack() {
			return hitCallBack;
		}

		/**
		 * 套打回电话
		 *
		 * @param hitCallBack 打回电话
		 * @since 2022-04-27 17:25:43
		 */
		public void setHitCallBack(HitCallBack hitCallBack) {
			this.hitCallBack = hitCallBack;
		}
	}

	/**
	 * 规则分析器
	 *
	 * @author shuigedeng
	 * @version 2022.04
	 * @since 2022-04-27 17:25:44
	 */
	static public class RulesAnalyzer {

		/**
		 * 规则
		 */
		private final Map<String, List<RuleInfo>> rules = new HashMap<>();
		/**
		 * 规则解析器
		 */
		private final RuleParser ruleParser = new RuleParser();

		/**
		 * 规则分析仪
		 *
		 * @return
		 * @since 2022-04-27 17:25:44
		 */
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
		 * @return {@link List }<{@link RuleInfo }>
		 * @since 2022-04-27 17:25:44
		 */
		public List<RuleInfo> parserRules(String rules) {
			return ruleParser.parser(rules);
		}

		/**
		 * getRules
		 *
		 * @param field field
		 * @return {@link List }<{@link RuleInfo }>
		 * @since 2022-04-27 17:25:44
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
		 * @since 2022-04-27 17:25:44
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
		 * @since 2022-04-27 17:25:44
		 */
		public void registerRules(String field, String rules) {
			registerRules(field, ruleParser.parser(rules));
		}

		/**
		 * registerRulesByProperties
		 *
		 * @param field field
		 * @since 2022-04-27 17:25:44
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
		 * @return {@link Report }
		 * @since 2022-04-27 17:25:44
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
	 * @version 2022.04
	 * @since 2022-04-27 17:25:44
	 */
	public static enum RuleType {
		more(">", "大于"),
		less("<", "小于"),
		equal("=", "等于"),
		contain("%", "包含");

		private final String desc;
		private final String tag;

		/**
		 * 规则类型
		 *
		 * @param tag  标签
		 * @param desc desc
		 * @return
		 * @since 2022-04-27 17:25:44
		 */
		RuleType(String tag, String desc) {
			this.desc = desc;
			this.tag = tag;
		}

		/**
		 * 得到规则类型
		 *
		 * @param tag 标签
		 * @return {@link RuleType }
		 * @since 2022-04-27 17:25:44
		 */
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
