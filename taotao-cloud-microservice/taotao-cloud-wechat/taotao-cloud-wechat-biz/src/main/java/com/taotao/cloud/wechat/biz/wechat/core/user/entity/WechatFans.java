package com.taotao.cloud.wechat.biz.wechat.core.user.entity;

import cn.bootx.common.core.function.EntityBaseFunction;
import cn.bootx.common.mybatisplus.base.MpIdEntity;
import cn.bootx.starter.wechat.core.user.convert.WechatFansConvert;
import cn.bootx.starter.wechat.dto.user.WechatFansDto;
import cn.bootx.starter.wechat.param.user.WechatFansParam;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
* 微信粉丝
* @author xxm
* @date 2022/7/15
*/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("starter_wx_fans")
public class WechatFans extends MpIdEntity implements EntityBaseFunction<WechatFansDto> {

    /** 关联OpenId */
    private String openid;

    /** unionId */
    private String unionId;

    /** 订阅状态，未关注/已关注 */
    private Boolean subscribe;

    /** 订阅时间 */
    private LocalDateTime subscribeTime;

    /** 语言 */
    private String language;

    /** 备注 */
    private String remark;

    /** 创建对象 */
    public static WechatFans init(WechatFansParam in) {
        return WechatFansConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public WechatFansDto toDto() {
        return WechatFansConvert.CONVERT.convert(this);
    }
}
