package com.taotao.cloud.recommend.biz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    /** 主键 */
    private Integer id;
    /** 年纪 */
    private Integer age;
    /** 性别 */
    private String sex;
    /** 职业 */
    private String profession;
    /** 邮编 */
    private String postcode;

}
