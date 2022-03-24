package com.taotao.cloud.operation.biz.entity;

import cn.hutool.http.HtmlUtil;
import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.enums.SwitchEnum;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.page.entity.enums.PageEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 页面数据DO
 */
@Data
@TableName("li_page_data")
@ApiModel(value = "页面数据DO")
@NoArgsConstructor
public class PageData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description =  "页面名称")

    private String name;

    @Schema(description =  "页面数据")
    private String pageData;

    /**
     * @see SwitchEnum
     */
    @Schema(description =  "页面开关状态", allowableValues = "OPEN,CLOSE")
    private String pageShow;

    /**
     * @see PageEnum
     */
    @Schema(description =  "页面类型", allowableValues = "INDEX,STORE,SPECIAL")
    private String pageType;

    /**
     * @see ClientTypeEnum
     */
    @Schema(description =  "客户端类型", allowableValues = "PC,H5,WECHAT_MP,APP")
    private String pageClientType;

    @Schema(description =  "值")
    private String num;

    public PageData(String name, String pageClientType, String pageData, String num) {
        this.name = name;
        this.pageClientType = pageClientType;
        this.pageData = pageData;
        this.num = num;
        this.pageShow = SwitchEnum.CLOSE.name();
        this.pageType = PageEnum.STORE.name();
    }

    public String getPageData() {
        if (StringUtils.isNotEmpty(pageData)) {
            return HtmlUtil.unescape(pageData);
        }
        return pageData;
    }
}
