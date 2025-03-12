package com.taotao.cloud.shortlink.biz.dcloud.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 刘森飚
 * @since 2023-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("link_group")
public class LinkGroupDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 组名
     */
    private String title;

    /**
     * 账号唯一编号
     */
    private Long accountNo;

    private Date gmtCreate;

    private Date gmtModified;


}
