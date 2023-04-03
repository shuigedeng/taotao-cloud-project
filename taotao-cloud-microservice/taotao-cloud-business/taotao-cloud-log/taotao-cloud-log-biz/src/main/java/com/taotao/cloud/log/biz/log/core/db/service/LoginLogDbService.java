package com.taotao.cloud.log.biz.log.core.db.service;

import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.audit.log.core.db.convert.LogConvert;
import cn.bootx.starter.audit.log.core.db.dao.LoginLogDbManager;
import cn.bootx.starter.audit.log.core.db.entity.LoginLogDb;
import cn.bootx.starter.audit.log.dto.LoginLogDto;
import cn.bootx.starter.audit.log.param.LoginLogParam;
import cn.bootx.starter.audit.log.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
* 登陆日志
* @author xxm
* @date 2021/8/12
*/
@Slf4j
@Service
@ConditionalOnProperty(prefix = "bootx.starter.audit-log", value = "store", havingValue = "jdbc",matchIfMissing = true)
@RequiredArgsConstructor
public class LoginLogDbService implements LoginLogService {
    private final LoginLogDbManager loginLogManager;

    /**
     * 添加
     */
    @Override
    public void add(LoginLogParam loginLog){
        loginLogManager.save(LogConvert.CONVERT.convert(loginLog));
    }

    /**
     * 获取
     */
    @Override
    public LoginLogDto findById(Long id){
        return loginLogManager.findById(id).map(LoginLogDb::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 分页
     */
    @Override
    public PageResult<LoginLogDto> page(PageParam pageParam, LoginLogParam loginLogParam){
        return MpUtil.convert2DtoPageResult(loginLogManager.page(pageParam,loginLogParam));
    }

    /**
     * 删除
     */
    @Override
    public void delete(Long id){
        loginLogManager.deleteById(id);
    }
}
