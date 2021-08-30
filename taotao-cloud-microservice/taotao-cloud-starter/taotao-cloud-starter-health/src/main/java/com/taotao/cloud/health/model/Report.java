package com.taotao.cloud.health.model;

import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.NumberUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.health.strategy.Rule;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: chejiangyi
 * @version: 2019-07-28 08:26 采集报表
 **/
public class Report extends LinkedHashMap<String, Object> implements Serializable {

	private String desc;

	public String getDesc() {
		return desc;
	}

	public Report setDesc(String desc) {
		this.desc =
			desc;
		return this;
	}

	private String name;

	public String getName() {
		return name;
	}

	public Report setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 求平均的报表
	 *
	 * @param reportList
	 * @return
	 */
	public Report avgReport(List<Report> reportList) {
		Map<String, Object> sums = new HashMap();
		//累加
		for (Report r : reportList) {
			eachReport(r, (fieldname, item) -> {
				if (sums.containsKey(fieldname)) {
					if (item.value != null && sums.get(fieldname) != null && sums.get(
						fieldname) instanceof Number && item.value instanceof Number) {
						sums.replace(fieldname, ((Number) sums.get(fieldname)).doubleValue()
							+ ((Number) item.value).doubleValue());
					}
				} else {
					if (item.value != null && item.value instanceof Number) {
						sums.put(fieldname, ((Number) item.value).doubleValue());
					}
				}
				return item;
			});
		}

		//求平均
		for (Map.Entry<String, Object> item : sums.entrySet()) {
			if (item.getValue() instanceof Number) {
				sums.replace(item.getKey(),
					NumberUtil.scale(((Number) item.getValue()).doubleValue() / reportList.size(),
						2));
			}
		}

		//生成平均报表
		Report report = reportList.get(reportList.size() - 1).clone();
		eachReport(report, (fieldname, item) -> {
			if (item.value instanceof Number) {
				item.setValue(sums.get(fieldname));
			}
			return item;
		});
		return report;
	}

	public String toHtml() {
		StringBuilder stringBuilder = new StringBuilder(
			String.format("<b>[%s(%s)]</b>\r\n", this.getName(), this.getDesc()));
		for (Map.Entry<String, Object> item : this.entrySet()) {
			if (item.getValue() instanceof ReportItem) {
				ReportItem reportItem = (ReportItem) item.getValue();
				Object value = reportItem.getValue();
				if (reportItem.getValue() != null && reportItem.getValue() instanceof Number) {
					value = NumberUtil.scale((Number) reportItem.getValue(), 2);
				} else if (reportItem.getValue() != null
					&& reportItem.getValue() instanceof String) {
					String text = StringUtil.nullToEmpty((String) reportItem.getValue());
					if ((item.getKey()).contains(".detail") && !text.isEmpty()) {
						value = "<span style='color:blue;cursor:pointer' title='{title}' onclick='this.innerHTML=(this.textContent==\"显示详情\"?this.title.replace(/\\r/g,\"\").replace(/\\n/g,\"<br/>\"):\"显示详情\");'>显示详情</span>"
							.replace("{title}",
								htmlEncode(text).replace("\r", "/r").replace("\n", "/n"));
						//.replace("{title2}",htmlEncode(text).replace("\r","").replace("\n","<br/>"));
					}
				}
				stringBuilder.append(
					String.format("%s(%s):%s%s\r\n", item.getKey(), reportItem.getDesc(), value,
						reportItem.isWarn() ? "<font color=\"#FF0000\">[报警]</font>" : ""));
			} else if (item.getValue() instanceof Report) {
				stringBuilder.append(((Report) item.getValue()).toHtml());
			}
		}
		stringBuilder.append("\r\n");
		return stringBuilder.toString();
	}

	public String htmlEncode(String source) {
		if (source == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			switch (c) {
				case '<':
					buffer.append("&lt;");
					break;
				case '>':
					buffer.append("&gt;");
					break;
				case '&':
					buffer.append("&amp;");
					break;
				case '"':
					buffer.append("&quot;");
					break;
				case '\'':
					buffer.append("&apos;");
					break;
				//case 10:
				//case 13:
				//    break;
				default:
					buffer.append(c);
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
			if (item.getValue() instanceof ReportItem) {
				callBack.run(item.getKey(), (ReportItem) item.getValue());
			} else if (item.getValue() instanceof Report) {
				eachReport((Report) item.getValue(), callBack);
			}
		}
	}

	public Report(Object info) {
		parseObject(this, info);
	}

	private void parseObject(Report report, Object obj) {
		for (Field field : obj.getClass().getDeclaredFields()) {
			FieldReport fieldReport = field.getAnnotation(FieldReport.class);
			if (fieldReport != null) {
				Object value = tryGet(field, obj);
				//null 不生成报表
				if (value == null) {

				} else if (value instanceof Number || value instanceof String) {
					report.put(fieldReport.name(),
						new ReportItem(fieldReport.desc(), value, "", null));
				} else {
					Report report2 = new Report().setDesc(fieldReport.desc())
						.setName(fieldReport.name());
					report.put(fieldReport.name(), report2);
					parseObject(report2, value);
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

	public interface ReportItemEachCallBack {

		ReportItem run(String field, ReportItem reportItem);
	}


	/**
	 * 深拷贝
	 *
	 * @return
	 */
	@Override
	public Report clone() {
		Report report = new Report().setName(this.name).setDesc(this.desc);
		for (Map.Entry<String, Object> item : this.entrySet()) {
			if (item.getValue() instanceof ReportItem) {
				report.put(item.getKey(), (((ReportItem) item.getValue()).clone()));
			} else if (item.getValue() instanceof Report) {
				report.put(item.getKey(), (((Report) item.getValue()).clone()));
			}
		}
		return report;
	}


	public static class ReportItem implements Serializable {

		private String desc;
		private Object value;
		private String warn;
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
}
