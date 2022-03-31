package com.taotao.cloud.bulletin.biz.entity;

import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 页面数据DO
 *
 * 
 * @since 2020/12/10 17:42
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
