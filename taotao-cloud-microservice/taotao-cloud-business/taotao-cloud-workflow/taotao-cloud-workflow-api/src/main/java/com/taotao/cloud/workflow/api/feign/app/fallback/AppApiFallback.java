package com.taotao.cloud.workflow.api.feign.app.fallback;

import jnpf.app.AppApi;
import lombok.extern.slf4j.Slf4j;

/**
 * api接口
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司（https://www.jnpfsoft.com）
 * @date 2021/3/15 11:55
 */
//@Component
@Slf4j
public class AppApiFallback implements AppApi {

    @Override
    public void deleObject(String id) {

    }
}
