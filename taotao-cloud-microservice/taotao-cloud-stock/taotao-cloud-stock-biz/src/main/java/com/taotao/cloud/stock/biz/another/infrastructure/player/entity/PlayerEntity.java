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
