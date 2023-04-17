package com.taotao.cloud.log.biz.dcloud.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
