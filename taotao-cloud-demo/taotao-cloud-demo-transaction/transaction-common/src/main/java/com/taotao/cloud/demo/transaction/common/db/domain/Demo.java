package com.taotao.cloud.demo.transaction.common.db.domain;

import com.codingapi.txlcn.common.util.id.RandomUtils;

import java.util.Date;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
public class Demo {
    private Long id;
    private String kid = RandomUtils.randomKey();
    private String demoField;
    private String groupId;
    private Date createTime;
    private String appName;

}
