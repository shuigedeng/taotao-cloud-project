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

package com.taotao.cloud.workflow.biz.common.model.visiual.fields;

import com.taotao.cloud.workflow.biz.common.model.visiual.fields.config.ConfigModel;
import com.taotao.cloud.workflow.biz.common.model.visiual.fields.props.PropsModel;
import com.taotao.cloud.workflow.biz.common.model.visiual.fields.slot.SlotModel;
import java.util.List;
import lombok.Data;

/** */
@Data
public class FieLdsModel {
    private ConfigModel config;
    private SlotModel slot;
    private String placeholder;
    private Object style;
    private Boolean clearable;
    private String prefixicon;
    private Integer precision;
    private String suffixicon;
    private String maxlength;
    private Boolean showWordLimit;
    private Boolean readonly;
    private Boolean disabled;
    /** 设置默认值为空字符串 */
    private String vModel = "";
    /** 关联表单id */
    private String modelId = "";
    /** 关联表单 二维码 条形码 字段 */
    private String relationField;

    private Boolean hasPage;
    private String pageSize;
    private String type;
    private Object autosize;
    private Integer step;
    private Boolean stepstrictly;
    private String controlsposition;
    private Object textStyle;
    private Integer lineHeight;
    private Integer fontSize;
    private Boolean showChinese;
    private Boolean showPassword;

    /** 大小 */
    private String size;

    private Boolean filterable;
    /** 关联表单属性 */
    private String showField;
    /** 多选 */
    private Boolean multiple = false;
    /** 待定 */
    private PropsModel props;
    /** 待定 */
    private Boolean showAllLevels;

    private String separator;
    private Boolean isrange;
    private String rangeseparator;
    private String startplaceholder;
    private String endplaceholder;
    private String format;
    private String valueformat;
    private Object pickeroptions;
    private Integer max;
    private Boolean allowhalf;
    private Boolean showText;
    private Boolean showScore;
    private Boolean showAlpha;
    private String colorformat;
    private String activetext;
    private String inactivetext;
    private String activecolor;
    private String inactivecolor;
    private String activevalue;
    private String inactivevalue;
    private Integer min;
    private Boolean showStops;
    private Boolean range;
    private String content;
    private String header;
    private Boolean accordion;
    private String tabPosition;
    /** 未找到 */
    private String accept;

    private Boolean showTip;
    private Integer fileSize;
    private String sizeUnit;
    private Integer limit;
    private String contentposition;
    private String buttonText;
    private Integer level;
    private String options;
    private String actionText;
    private String shadow;
    private String name;
    private String title;

    /** 查询方式 1.eq 2.like 3.between */
    private Integer searchType;

    private String interfaceId;
    private List<ColumnOptionModel> columnOptions;
    private String propsValue;

    /** 开关 值 */
    private String activeTxt = "开";

    private String inactiveTxt = "关";

    /** 条形码 条码颜色 */
    private String lineColor;
    /** 条形码 背景色 */
    private String background;
    /** 条形码 宽高 */
    private Integer width;

    private Integer height;
    /** 条形码 二维码 固定值 */
    private String staticText;

    /** 条形码 二维码 类型 （静态,或者组件,当前表单路径） static relation form */
    private String dataType = "";

    /** 二维码 条码颜色 */
    private String colorDark;

    /** 二维码 背景色 */
    private String colorLight;

    /** 按钮(居中,右,左) */
    private String align;

    /** 子表是否合计 */
    private Boolean showSummary;

    /** 子表合计字段 */
    private String summaryField;

    /** 所属部门展示内容 */
    private String showLevel;

    /** 弹窗 样式属性 */
    private String popupType;

    private String popupTitle;
    private String popupWidth;
}
