package com.taotao.cloud.workflow.api.model.visiual.fields.config;


import com.taotao.cloud.workflow.api.model.visiual.fields.FieLdsModel;
import java.util.List;
import lombok.Data;

@Data
public class ConfigModel {

	private String label;
	private String labelWidth;
	private Boolean showLabel;
	private Boolean changeTag;
	private Boolean border;
	private String tag;
	private String tagIcon;
	private boolean required = false;
	private String layout;
	private String dataType;
	private Integer span = 24;
	private String flowKey;
	private String dictionaryType;
	private Integer formId;
	private Long renderKey;
	private Integer columnWidth;
	private List<RegListModel> regList;
	private Object defaultValue;
	private String active;
	/**
	 * app静态数据
	 */
	private String options;
	/**
	 * 判断defaultValue类型
	 */
	private String valueType;
	private String propsUrl;
	private String optionType;
	private ConfigPropsModel props;
	/**
	 * 子表添加字段
	 */
	private Boolean showTitle;
	private String tableName;
	private List<FieLdsModel> children;

	/**
	 * 多端显示
	 */
	private String visibility = "[\"app\",\"pc\"]";

	/**
	 * 单据规则使用
	 */
	private String rule;

	/**
	 * 验证规则触发方式
	 */
	private String trigger = "blur";
	/**
	 * 隐藏
	 */
	private Boolean noShow = false;
	/**
	 * app代码生成器
	 */
	private int childNum;
	private String model;

	/**
	 * 代码生成器多端显示
	 */
	private boolean app = true;
	private boolean pc = true;
}
