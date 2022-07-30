package com.taotao.cloud.wechat.biz.wecom.core.robot.entity;

import cn.bootx.common.core.function.EntityBaseFunction;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.starter.wecom.code.WeComCode;
import cn.bootx.starter.wecom.core.robot.convert.WecomRobotConfigConvert;
import cn.bootx.starter.wecom.dto.robot.WecomRobotConfigDto;
import cn.bootx.starter.wecom.param.robot.WecomRobotConfigParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* 企业微信机器人配置
* @author bootx
* @date 2022-07-23
*/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("starter_wecom_robot_config")
@Accessors(chain = true)
public class WecomRobotConfig extends MpBaseEntity implements EntityBaseFunction<WecomRobotConfigDto>{

    /** 名称 */
    private String name;
    /** 编号 */
    private String code;
    /** webhook地址的key */
    private String webhookKey;
    /** 备注 */
    private String remark;

    /**
     * 获取webhook的地址
     */
    public String toWebhookUrl() {
        return StrUtil.format(WeComCode.ROBOT_WEBHOOK_URL, webhookKey);
    }

    /** 创建对象 */
    public static WecomRobotConfig init(WecomRobotConfigParam in) {
            return WecomRobotConfigConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public WecomRobotConfigDto toDto() {
        return WecomRobotConfigConvert.CONVERT.convert(this);
    }
}
