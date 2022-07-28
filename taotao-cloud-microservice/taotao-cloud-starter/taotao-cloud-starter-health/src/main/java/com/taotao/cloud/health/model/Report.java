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
package com.taotao.cloud.health.model;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.number.NumberUtil;
import com.taotao.cloud.health.annotation.FieldReport;
import com.taotao.cloud.health.strategy.Rule;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 采集报表
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 10:44:01
 */
public class Report extends LinkedHashMap<String, Object> implements Serializable {

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 求平均报表
	 *
	 * @param reportList reportList
	 * @return {@link com.taotao.cloud.health.model.Report }
	 * @author shuigedeng
	 * @since 2021-09-10 10:45:16
	 */
	public Report avgReport(List<Report> reportList) {
		Map<String, Object> sums = new HashMap<>();

		//累加
		for (Report r : reportList) {
			eachReport(r, (fieldName, item) -> {
				Object value = item.value;
				if (sums.containsKey(fieldName)) {
					Object fieldValue = sums.get(fieldName);

					if (fieldValue instanceof Number && value instanceof Number) {
						sums.replace(fieldName,
							((Number) fieldValue).doubleValue() + ((Number) value).doubleValue());
					}
				} else {
					if (value instanceof Number) {
						sums.put(fieldName, ((Number) value).doubleValue());
					}
				}
				return item;
			});
		}

		//求平均
		for (Map.Entry<String, Object> item : sums.entrySet()) {
			Object value = item.getValue();
			if (value instanceof Number) {
				if (!reportList.isEmpty()) {
					sums.replace(item.getKey(),
						NumberUtil.scale(((Number) value).doubleValue() / reportList.size(), 2));
				}
			}
		}

		//生成平均报表
		Report report = reportList.get(reportList.size() - 1).clone();
		eachReport(report, (fieldName, item) -> {
			if (item.value instanceof Number) {
				item.setValue(sums.get(fieldName));
			}
			return item;
		});
		return report;
	}

	public String toHtml() {
		StringBuilder stringBuilder = new StringBuilder(
			String.format("<b>[%s(%s)]</b>\r\n", this.getName(), this.getDesc()));

		for (Map.Entry<String, Object> item : this.entrySet()) {
			Object value = item.getValue();
			if (value instanceof ReportItem reportItem) {
				Object itemValue = reportItem.getValue();

				if (Objects.nonNull(itemValue)) {
					if (itemValue instanceof Number) {
						itemValue = NumberUtil.scale((Number) itemValue, 2);
					} else if (itemValue instanceof String) {
						String text = StringUtil.nullToEmpty(itemValue);
						if ((item.getKey()).contains(".detail") && !text.isEmpty()) {
							itemValue = ("<span style='color:blue;cursor:pointer' title='{title}' "
								+ "onclick='this.innerHTML=(this.textContent==\"显示详情\"?"
								+ "this.title.replace(/\\r/g,\"\").replace(/\\n/g,\"<br/>\"):\"显示详情\");'>显示详情</span>")
								.replace("{title}", htmlEncode(text)
									.replace("\r", "/r")
									.replace("\n", "/n"));
						}
					} else {
						itemValue = JsonUtil.toJSONString(itemValue);
					}
				} else {
					itemValue = "NULL";
				}

				stringBuilder.append(
					String.format("%s(%s):%s%s\r\n",
						item.getKey(),
						reportItem.getDesc(),
						itemValue,
						reportItem.isWarn() ? "<font color=\"#FF0000\">[报警]</font>" : ""));
			} else if (value instanceof Report) {
				stringBuilder.append(((Report) value).toHtml());
			}
		}
		stringBuilder.append("\r\n");
		return stringBuilder.toString();
	}

	public String htmlEncode(String source) {
		if (source == null) {
			return "";
		}

		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			switch (c) {
				case '<' -> buffer.append("&lt;");
				case '>' -> buffer.append("&gt;");
				case '&' -> buffer.append("&amp;");
				case '"' -> buffer.append("&quot;");
				case '\'' -> buffer.append("&apos;");

				//case 10:
				//case 13:
				//    break;
				default -> buffer.append(c);
			}
		}
		return buffer.toString();
	}

	public String toJson() {
		return JsonUtil.toJSONString(this);
	}

	public void eachReport(ReportItemEachCallBack callBack) {
		eachReport(this, callBack);
	}

	private void eachReport(Report report, ReportItemEachCallBack callBack) {
		for (Map.Entry<String, Object> item : report.entrySet()) {
			Object value = item.getValue();

			if (value instanceof ReportItem) {
				callBack.run(item.getKey(), (ReportItem) value);
			} else if (item.getValue() instanceof Report) {
				eachReport((Report) value, callBack);
			}
		}
	}

	public Report(CollectInfo info) {
		parseObject(this, info);
	}

	private void parseObject(Report report, CollectInfo obj) {
		for (Field field : obj.getClass().getDeclaredFields()) {
			FieldReport fieldReport = field.getAnnotation(FieldReport.class);
			if (fieldReport != null) {
				Object value = tryGet(field, obj);
				//null 不生成报表
				if (value == null) {

				} else if (value instanceof Number) {
					report.put(fieldReport.name(),
						new ReportItem(fieldReport.desc(), value, "", null));
				} else if (value instanceof CollectInfo) {
					Report report2 = new Report()
						.setDesc(fieldReport.desc())
						.setName(fieldReport.name());

					report.put(fieldReport.name(), report2);
					parseObject(report2, (CollectInfo) value);
				} else {
					report.put(fieldReport.name(),
						new ReportItem(fieldReport.desc(), JsonUtil.toJSONString(value), "", null));
				}
			}
		}
	}

	private Object tryGet(Field field, Object obj) {
		try {
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			return null;
		}
	}

	public Report() {
	}

	/**
	 * ReportItemEachCallBack
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-10 10:47:02
	 */
	public interface ReportItemEachCallBack {

		/**
		 * run
		 *
		 * @param field      field
		 * @param reportItem reportItem
		 * @return {@link com.taotao.cloud.health.model.Report.ReportItem }
		 * @author shuigedeng
		 * @since 2021-09-10 10:47:06
		 */
		ReportItem run(String field, ReportItem reportItem);
	}

	@Override
	public Report clone() {
		Report report = new Report()
			.setName(this.name)
			.setDesc(this.desc);

		for (Map.Entry<String, Object> item : this.entrySet()) {
			Object value = item.getValue();

			if (value instanceof ReportItem) {
				report.put(item.getKey(), (((ReportItem) value).clone()));
			} else if (value instanceof Report) {
				report.put(item.getKey(), (((Report) value).clone()));
			}
		}
		return report;
	}

	/**
	 * ReportItem
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-10 10:45:55
	 */
	public static class ReportItem implements Serializable {

		/**
		 * 描述
		 */
		private String desc;
		/**
		 * 值
		 */
		private Object value;
		/**
		 * warn
		 */
		private String warn;
		/**
		 * 规则
		 */
		private transient Rule.RuleInfo rule;

		public ReportItem(String desc, Object value, String warn, Rule.RuleInfo rule) {
			this.desc = desc;
			this.value = value;
			this.warn = warn;
			this.rule = rule;
		}

		public boolean isWarn() {
			return warn != null && !warn.isEmpty();
		}

		@Override
		public String toString() {
			return "ReportItem{" +
				"desc='" + desc + '\'' +
				", value=" + value +
				", warn='" + warn + '\'' +
				", rule=" + rule +
				'}';
		}

		@Override
		public ReportItem clone() {
			return new ReportItem(desc, value, warn, rule);
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String getWarn() {
			return warn;
		}

		public void setWarn(String warn) {
			this.warn = warn;
		}

		public Rule.RuleInfo getRule() {
			return rule;
		}

		public void setRule(Rule.RuleInfo rule) {
			this.rule = rule;
		}
	}

	public String getDesc() {
		return desc;
	}

	public Report setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public String getName() {
		return name;
	}

	public Report setName(String name) {
		this.name = name;
		return this;
	}


}
