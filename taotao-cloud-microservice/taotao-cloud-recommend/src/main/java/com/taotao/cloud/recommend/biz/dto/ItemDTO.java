package com.taotao.cloud.recommend.biz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    /** 主键 */
    private Integer id;
    /** 名称 */
    private String name;
    /** 日期 */
    private String date;
    /** 链接 */
    private String link;

}
