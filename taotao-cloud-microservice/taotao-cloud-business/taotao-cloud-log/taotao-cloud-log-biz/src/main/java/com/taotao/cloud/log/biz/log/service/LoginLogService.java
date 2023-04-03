package com.taotao.cloud.log.biz.log.service;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.starter.audit.log.dto.LoginLogDto;
import cn.bootx.starter.audit.log.param.LoginLogParam;
import org.springframework.scheduling.annotation.Async;

/**   
* 登陆日志
* @author xxm  
* @date 2021/12/2 
*/
public interface LoginLogService {

    /**
     * 添加
     */
    @Async("asyncExecutor")
    void add(LoginLogParam loginLog);

    /**
     * 获取
     */
    LoginLogDto findById(Long id);

    /**
     * 分页
     */
    PageResult<LoginLogDto> page(PageParam pageParam, LoginLogParam loginLogParam);

    /**
     * 删除
     */
    void delete(Long id);
}
