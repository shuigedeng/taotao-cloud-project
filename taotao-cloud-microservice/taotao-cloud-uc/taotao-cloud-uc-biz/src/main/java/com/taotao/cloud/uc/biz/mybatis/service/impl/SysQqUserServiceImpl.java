// package com.taotao.cloud.uc.biz.service.impl;
//
//
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.taotao.cloud.common.enums.ResultEnum;
// import com.taotao.cloud.common.model.PageResult;
// import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
// import com.taotao.cloud.uc.biz.entity.SysQqUser;
// import com.taotao.cloud.uc.biz.mapper.SysQqUserMapper;
// import com.taotao.cloud.uc.biz.service.ISysQqUserService;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Service;
//
// import java.util.Collections;
// import java.util.Map;
//
// /**
//  * qq用户表
//  *
//  * @author taotao
//  * @date 2020-05-14 14:36:39
//  */
// @Slf4j
// @Service
// public class SysQqUserServiceImpl extends SuperServiceImpl<SysQqUserMapper, SysQqUser> implements ISysQqUserService {
//     @Override
//     public PageResult<SysQqUser> findList(Map<String, Object> params) {
// //        Page<SysQqUser> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
//         Page<SysQqUser> page = new Page<>(1, 10);
//         LambdaQueryWrapper<SysQqUser> qqUserLambdaQueryWrapper = Wrappers.<SysQqUser>lambdaQuery();
//         Page<SysQqUser> pageResult = baseMapper.selectPage(page, qqUserLambdaQueryWrapper);
//
//         PageResult result = PageResult.builder().currentPage(page.getCurrent()).total(pageResult.getTotal())
//                 .code(ResultEnum.SUCCESS.getCode())
//                 .pageSize(pageResult.getSize())
//                 .data(Collections.singletonList(pageResult.getRecords()))
//                 .build();
//
//         return result;
//     }
// }
