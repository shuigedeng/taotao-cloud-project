package com.taotao.cloud.log.biz.log.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.data.mybatisplus.pagehelper.PageParam;
import com.taotao.cloud.log.biz.log.dto.LoginLogDto;
import com.taotao.cloud.log.biz.log.param.LoginLogParam;
import org.springframework.scheduling.annotation.Async;

/**   
* 登陆日志
* @author shuigedeng  
* @date 2021/12/2 
*/
public interface LoginLogService {

    /**
     * 添加
     */
    void add(LoginLogParam loginLog);

    /**
     * 获取
     */
    LoginLogDto findById(Long id);

    /**
     * 分页
     */
    PageResult<LoginLogDto> page(LoginLogParam loginLogParam);

    /**
     * 删除
     */
    void delete(Long id);
}
