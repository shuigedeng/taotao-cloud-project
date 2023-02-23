package com.taotao.cloud.wechat.biz.wecom.core.robot.convert;

import cn.bootx.starter.wecom.core.robot.entity.WecomRobotConfig;
import cn.bootx.starter.wecom.dto.robot.WecomRobotConfigDto;
import cn.bootx.starter.wecom.param.robot.WecomRobotConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 企业微信机器人配置
 * @author bootx
 * @date 2022-07-23
 */
@Mapper
public interface WecomRobotConfigConvert {
    WecomRobotConfigConvert CONVERT = Mappers.getMapper(WecomRobotConfigConvert.class);

    WecomRobotConfig convert(WecomRobotConfigParam in);

    WecomRobotConfigDto convert(WecomRobotConfig in);

}
