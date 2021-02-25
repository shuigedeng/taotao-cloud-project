// package com.taotao.cloud.uc.biz.service.impl;
//
// import cn.hutool.core.util.StrUtil;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
// import com.taotao.cloud.log.model.SysLog;
// import com.taotao.cloud.uc.biz.mapper.SysLogMapper;
// import com.taotao.cloud.uc.biz.service.ISysLogService;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.io.Serializable;
//
// /**
//  * 系统日志 服务实现类
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:36
//  */
// @Service
// public class SysLogServiceImpl extends SuperServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
//
//     @Override
//     @Transactional(rollbackFor = Exception.class)
//     public boolean save(SysLog entity) {
//         return super.save(entity);
//     }
//
//     @Override
//     public IPage<SysLog> selectLogList(Integer page, Integer pageSize, Integer type, String userName) {
//         Page<SysLog> logPage = new Page<>(page, pageSize);
//         LambdaQueryWrapper<SysLog> sysLogLambdaQueryWrapper = Wrappers.<SysLog>lambdaQuery().eq(SysLog::getType, type).orderByDesc(SysLog::getStartTime);
//         if (StrUtil.isNotEmpty(userName)) {
//             sysLogLambdaQueryWrapper.like(SysLog::getUserName, userName);
//         }
//         return baseMapper.selectPage(logPage, sysLogLambdaQueryWrapper);
//     }
//
//     @Override
//     @Transactional(rollbackFor = Exception.class)
//     public boolean removeById(Serializable id) {
//         return super.removeById(id);
//     }
// }
