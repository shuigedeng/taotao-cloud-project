package com.taotao.cloud.recommend.biz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关系数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelateDTO {
    /** 用户id */
    private Integer useId;
    /** 物品id */
    private Integer itemId;
    /** 指数 */
    private Integer index;


}
