package com.taotao.cloud.wechat.biz.wechat.util;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

/**   
 * 微信工具类
 * @author xxm  
 * @date 2022/8/5 
 */
@UtilityClass
public class WeChatUtil {

    /**
     * 获取微信二维码链接的参数值
     */
    public String getKeyByUrl(String url){
        return StrUtil.subAfter(url,"/q/",true);
    }
}
