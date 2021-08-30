package com.taotao.cloud.health.strategy;

import com.taotao.cloud.common.utils.BeanUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.model.PropertyCache;
import com.taotao.cloud.core.utils.PropertyUtil;
import com.taotao.cloud.health.model.Report;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

/**
 * @author: chejiangyi
 * @version: 2019-07-28 11:48 规则引擎
 **/
public class Rule {

	/**
	 * 规则
	 */
	public static class RuleInfo implements Serializable {

		private RuleType type;
		private Object value;
		private HitCallBack hitCallBack;

		@Override
		public String toString() {
			return type.tag + value.toString();
		}

		public boolean check(Object checkvalue) {
			if (checkvalue == null) {
				return false;
			}
			try {
				if (checkvalue instanceof Number) {
					double checkvaluevalue2 = ((Number) checkvalue).doubleValue();
					double warnvalue = (BeanUtil.convert(value, Number.class)).doubleValue();
					if (type == RuleType.less && checkvaluevalue2 < warnvalue) {
						return true;
					} else if (type == RuleType.more && checkvaluevalue2 > warnvalue) {
						return true;
					} else if (type == RuleType.equal && checkvaluevalue2 == warnvalue) {
						return true;
					}
				} else {
					String checkvaluevalue2 = checkvalue.toString();
					String warnvalue = value.toString();
					if (type == RuleType.equal && checkvaluevalue2 == warnvalue) {
						return true;
					} else if (type == RuleType.contain && checkvaluevalue2.contains(warnvalue)) {
						return true;
					}
				}
			} catch (Exception exp) {
				LogUtil.error("health", "check 规则检查出错", exp);
			}
			return false;
		}

		public RuleInfo() {
		}

		public RuleInfo(RuleType type, Object value,
			HitCallBack hitCallBack) {
			this.type = type;
			this.value = value;
			this.hitCallBack = hitCallBack;
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
	 */
	static public class RulesAnalyzer {

		private Map<String, List<RuleInfo>> rules = new HashMap<>();
		private RuleParser ruleParser = new RuleParser();

		public RulesAnalyzer() {
			//订阅配置改变，重新注册规则
			PropertyCache.DEFAULT.listenUpdateCache("RulesAnalyzer 动态规则订阅", (map) -> {
				for (Map.Entry<String, Object> e : map.entrySet()) {
					String key = e.getKey();
					if (StringUtils.startsWithIgnoreCase(key, "taotao.cloud.health.strategy.")) {
						key = key.replace("taotao.cloud.health.strategy.", "");
						Object rule = rules.get(key);
						if (rule != null) {
							registerRules(key,
								StringUtil.nullToEmpty(e.getValue()));
						}
					}
				}
			});
		}

		public List<RuleInfo> parserRules(String rules) {
			return ruleParser.parser(rules);
		}

		public List<RuleInfo> getRules(String field) {
			List<RuleInfo> item = rules.get(field);
			if (item == null) {
				registerRulesByProperties(field);
			}
			return rules.get(field);
		}

		public void registerRules(String field, List<RuleInfo> rules) {
//            if(this.rules.containsKey(field)) {
//                this.rules.get(field).addAll(rules);
//
//            }else{
			this.rules.put(field, rules);
//            }
		}

		public void registerRules(String field, String rules) {
			registerRules(field, ruleParser.parser(rules));
		}

		public void registerRulesByProperties(String field) {
			String value = PropertyUtil.getPropertyCache("taotao.cloud.health.strategy." + field, "");
			registerRules(field, value);
		}

		public Report analyse(Report report) {
			report.eachReport((fieldname, item) -> {
				List<RuleInfo> rules = this.getRules(fieldname);
				if (rules != null) {
					for (RuleInfo ruleInfo : rules) {
						boolean isWarn = ruleInfo.check(item.getValue());
						if (isWarn == true) {
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

	public static enum RuleType {
		more(">", "大于"),
		less("<", "小于"),
		equal("=", "等于"),
		contain("%", "包含");

		private String desc;
		private String tag;

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
	 */
	public static class RuleParser {

		public List<RuleInfo> parser(String text) {
			List<RuleInfo> result = new ArrayList<>();
			try {
				if (text.startsWith("[") && text.endsWith("]")) {
					text = text.replace("[", "").replace("]", "");
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
			} catch (Exception exp) {
				LogUtil.error("health", "parser规则解析出错", exp);
			}
			return result;
		}
	}

	/**
	 * 命中回调
	 */
	public interface HitCallBack {

		void run(Object value);
	}
}
