/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.stock.biz.another.infrastructure.player.entity;

import java.util.Date;
import lombok.Data;

/**
 * CREATE TABLE `player` ( `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键', `player_id`
 * varchar(256) NOT NULL COMMENT '运动员编号', `player_name` varchar(256) NOT NULL COMMENT '运动员名称',
 * `height` int(11) NOT NULL COMMENT '身高', `weight` int(11) NOT NULL COMMENT '体重',
 * `game_performance` text COMMENT '最近一场比赛表现', `creator` varchar(256) NOT NULL COMMENT '创建人',
 * `updator` varchar(256) NOT NULL COMMENT '修改人', `create_time` datetime NOT NULL COMMENT '创建时间',
 * `update_time` datetime NOT NULL COMMENT '修改时间', PRIMARY KEY (`id`) ) ENGINE=InnoDB
 * AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
 */
@Data
public class PlayerEntity {

    private Long id;
    private String playerId;
    private String playerName;
    private Integer height;
    private Integer weight;
    private String creator;
    private String updator;
    private Date createTime;
    private Date updateTime;
    private String gamePerformance;
}
