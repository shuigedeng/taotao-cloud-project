package com.taotao.cloud.sys.biz.entity.system.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.sys.biz.entity.system.entity.dos.AppVersion;
import com.taotao.cloud.sys.biz.entity.system.mapper.AppVersionMapper;
import com.taotao.cloud.sys.biz.entity.system.service.AppVersionService;
import org.springframework.stereotype.Service;


/**
 * APP版本控制业务层实现
 *
 * @author Chopper
 * @since 2020/11/17 8:02 下午
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements
	AppVersionService {

    @Override
    public AppVersion getAppVersion(String appType) {
        return this.baseMapper.getLatestVersion(appType);
    }

    @Override
    public boolean checkAppVersion(AppVersion appVersion) {
        //检测版本是否存在
        if (null != this.getOne(new LambdaQueryWrapper<AppVersion>()
                .eq(AppVersion::getVersion, appVersion.getVersion())
                .ne(appVersion.getId() != null, AppVersion::getId, appVersion.getId()))) {
            throw new ServiceException(ResultCode.APP_VERSION_EXIST);
        }
        return true;
    }
}
