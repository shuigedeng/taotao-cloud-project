package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* <p>
* 租户第三方登录功能配置表
* </p>
*
* @author GitEgg
* @since 2022-05-16
*/
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@ApiModel(value="JustAuthConfig对象", description="租户第三方登录功能配置表数据导入模板")
public class JustAuthConfigImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录开关")
    @ExcelProperty(value = "登录开关" ,index = 0)
    @ColumnWidth(20)
    private Boolean enabled;

    @ApiModelProperty(value = "配置类")
    @ExcelProperty(value = "配置类" ,index = 1)
    @ColumnWidth(20)
    private String enumClass;

    @ApiModelProperty(value = "Http超时")
    @ExcelProperty(value = "Http超时" ,index = 2)
    @ColumnWidth(20)
    private Integer httpTimeout;

    @ApiModelProperty(value = "缓存类型")
    @ExcelProperty(value = "缓存类型" ,index = 3)
    @ColumnWidth(20)
    private String cacheType;

    @ApiModelProperty(value = "缓存前缀")
    @ExcelProperty(value = "缓存前缀" ,index = 4)
    @ColumnWidth(20)
    private String cachePrefix;

    @ApiModelProperty(value = "缓存超时")
    @ExcelProperty(value = "缓存超时" ,index = 5)
    @ColumnWidth(20)
    private Integer cacheTimeout;

    @ApiModelProperty(value = "状态")
    @ExcelProperty(value = "状态" ,index = 6)
    @ColumnWidth(20)
    private Integer status;

    @ApiModelProperty(value = "备注")
    @ExcelProperty(value = "备注" ,index = 7)
    @ColumnWidth(20)
    private String remark;
}
