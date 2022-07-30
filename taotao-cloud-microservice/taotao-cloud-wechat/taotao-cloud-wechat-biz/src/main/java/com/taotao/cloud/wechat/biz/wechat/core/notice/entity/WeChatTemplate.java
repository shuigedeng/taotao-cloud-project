package com.taotao.cloud.wechat.biz.wechat.core.notice.entity;

import cn.bootx.common.mybatisplus.base.MpDelEntity;
import cn.bootx.starter.wechat.core.notice.convert.WeChatTemplateConvert;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;

/**   
* 微信消息模板
* @author xxm  
* @date 2022/7/16 
*/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("starter_wx_template")
public class WeChatTemplate extends MpDelEntity {

    /** 名称 */
    private String name;

    /** 编码 */
    private String code;

    /** 是否启用 */
    private boolean enable;

    /** 模板ID */
    private String templateId;

    /** 模板标题 */
    private String title;

    /** 模板所属行业的一级行业 */
    private String primaryIndustry;

    /** 模板所属行业的二级行业 */
    private String deputyIndustry;

    /** 模板内容 */
    private String content;

    /** 示例 */
    private String example;

    public static WeChatTemplate init(WxMpTemplate wxMpTemplate){
        WeChatTemplate template = WeChatTemplateConvert.CONVERT.convert(wxMpTemplate);
        template.setEnable(true);
        return template;
    }
}
