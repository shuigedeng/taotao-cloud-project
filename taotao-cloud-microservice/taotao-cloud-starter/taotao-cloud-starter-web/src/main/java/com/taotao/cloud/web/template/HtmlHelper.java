package com.taotao.cloud.web.template;

import com.taotao.cloud.common.utils.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.val;

/**
 * 底层模板方法的扩展，用于前台freemarker的java函数扩展，方便使用。 但是使用前要么在controller层（SpringMvcController）进行重新注入，要么在类似NetMvcHandlerInterceptor拦截器方式注入重载
 */
public class HtmlHelper extends SimpleTemplateProvider {

	public String substring3(String str, int maxlen) {
		return this.cutstring(str, maxlen);
	}

	public int totalpagenum(int totalRecord, int pageSize) {
		return (totalRecord + pageSize - 1) / pageSize + 1;
	}

	public String help(String str) {
		return String.format(
			"<img class='texthelp' width=\"20\" height=\"20\" title=\"%s\" style=\"\" src=\"/content/images/help.png\">",
			str);
	}

	public Boolean isnull(Object o) {
		return o == null;
	}

	public Object empty(Object o) {
		if (o == null || o.equals("null")) {
			return "";
		}
		return o;
	}

	public Object d(Object o, Object defaultValue) {
		if (o == null) {
			return defaultValue;
		} else {
			return o;
		}
	}

	public Object w2(String condition, Object data1, Object data2, Object trueObj,
		Object falseObj) {
		if (condition.equals("==")) {
			if (data1 == data2) {
				return trueObj;
			}
			if (data1 != null && data1.equals(data2)) {
				return trueObj;
			}
			return falseObj;
		}
		if (condition.equals(">")) {
			if (((Number) data1).doubleValue() > ((Number) data2).doubleValue()) {
				return trueObj;
			} else {
				return falseObj;
			}
		}
		if (condition.equals("<")) {
			if (((Number) data1).doubleValue() < ((Number) data2).doubleValue()) {
				return trueObj;
			} else {
				return falseObj;
			}
		}
		throw new RuntimeException("条件不符合规范");
	}

	public String enumDesc(String enumClass, Object value) {
		try {
			for (val item : Class.forName(enumClass).getEnumConstants()) {
				if (item == value || item.getClass().getField("Value").get(item).toString()
					.equals(value.toString())) {
					return item.getClass().getField("Desc").get(item).toString();
				}
			}
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	public List<Object> enums(String enumClass) {
		try {
			val objs = new ArrayList<Object>();
			for (val item : Class.forName(enumClass).getEnumConstants()) {
				objs.add(item);
			}
			return objs;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public Object filed(Object o, String filedName) {
		try {
			val f = o.getClass().getField(filedName);
			f.setAccessible(true);
			return f.get(o);
		} catch (Exception e) {
			return "";
		}
	}

	public Object filed2(Object o, String filedName) {
		try {
			val fs = filedName.split("\\.");
			Object d = o;
			for (val f : fs) {
				if (d != null) {
					val v = d.getClass().getDeclaredField(f);
					v.setAccessible(true);
					d = v.get(d);
				} else {
					return "";
				}
			}
			return d;
		} catch (Exception e) {
			return "";
		}
	}

	public String toJson(Object o) {
		return JsonUtil.toJSONString(o);
	}
}
