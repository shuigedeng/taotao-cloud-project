package com.taotao.cloud.message.biz.austin.handler.domain.wechat.robot;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

/**
 * 企业微信 机器人  返回值
 * https://developer.work.weixin.qq.com/document/path/91770#%E6%96%87%E6%9C%AC%E7%B1%BB%E5%9E%8B
 *
 * @author shuigedeng
 * @date 2022/12/26
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Accessors(chain=true)
public class EnterpriseWeChatRootResult {


    @JSONField(name = "errcode")
    private Integer errcode;
    @JSONField(name = "errmsg")
    private String errmsg;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "media_id")
    private String mediaId;
    @JSONField(name = "created_at")
    private String createdAt;
}
