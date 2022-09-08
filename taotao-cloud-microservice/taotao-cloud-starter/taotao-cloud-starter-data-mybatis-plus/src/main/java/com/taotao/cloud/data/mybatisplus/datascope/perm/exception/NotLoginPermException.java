package com.taotao.cloud.data.mybatisplus.datascope.perm.exception;

import cn.bootx.common.core.exception.BizException;

/**
* 未登录无法进行数据鉴权异常
* @author xxm
* @date 2022/5/5
*/
public class NotLoginPermException extends BizException {
    public NotLoginPermException() {
        super("未登录, 无法获取数据");
    }
}
