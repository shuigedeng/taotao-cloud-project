package com.taotao.cloud.product.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品VO
 *
 * @author dengtao
 * @date 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "商品VO", description = "商品VO")
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 5126530068827085130L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "图片id")
    private Long picId;

    @ApiModelProperty(value = "视频id")
    private Long videoId;

    @ApiModelProperty(value = "商品详情图片id")
    private Long detailPicId;

    @ApiModelProperty(value = "商品第一张图片id")
    private Long firstPicId;

    @ApiModelProperty(value = "商品海报id")
    private Long posterPicId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "商品状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime lastModifiedTime;
}
