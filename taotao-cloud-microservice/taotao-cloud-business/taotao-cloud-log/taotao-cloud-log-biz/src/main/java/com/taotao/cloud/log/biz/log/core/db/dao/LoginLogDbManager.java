package com.taotao.cloud.log.biz.log.core.db.dao;

import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.audit.log.core.db.entity.LoginLogDb;
import cn.bootx.starter.audit.log.param.LoginLogParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**   
* 登录日志
* @author xxm  
* @date 2021/8/12 
*/
@Slf4j
@Repository
@RequiredArgsConstructor
public class LoginLogDbManager extends BaseManager<LoginLogDbMapper, LoginLogDb> {

    public Page<LoginLogDb> page(PageParam pageParam, LoginLogParam loginLogParam) {
        Page<LoginLogDb> mpPage = MpUtil.getMpPage(pageParam, LoginLogDb.class);
        return lambdaQuery()
                .orderByDesc(LoginLogDb::getId)
                .like(StrUtil.isNotBlank(loginLogParam.getAccount()), LoginLogDb::getAccount,loginLogParam.getAccount())
                .like(StrUtil.isNotBlank(loginLogParam.getClient()), LoginLogDb::getClient,loginLogParam.getClient())
                .like(StrUtil.isNotBlank(loginLogParam.getLoginType()), LoginLogDb::getLoginType,loginLogParam.getLoginType())
                .page(mpPage);
    }
}
