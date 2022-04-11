/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.api.dto.log;


import java.sql.Timestamp;
import java.util.List;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LogQueryCriteria {

    private String blurry;

    private String logType;

    private List<Timestamp> createTime;

    private Integer type;

}
