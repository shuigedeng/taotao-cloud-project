//package com.taotao.cloud.data.mybatis.plus.controller;
//
//import com.baomidou.mybatisplus.extension.api.R;
//import com.taotao.cloud.common.annotation.log.SysLog;
//import com.taotao.cloud.common.annotation.security.PreAuth;
//import com.taotao.cloud.data.mybatis.plus.service.ISuperCacheService;
//import java.io.Serializable;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
///**
// * SuperCacheController
// * <p>
// * 继承该类，在SuperController类的基础上扩展了以下方法：
// * 1，get ： 根据ID查询缓存，若缓存不存在，则查询DB
// *
// * @author zuihou
// * @date 2020年03月06日11:06:46
// */
//public abstract class SuperCacheController<S extends ISuperCacheService<Entity>, Id extends Serializable, Entity, PageQuery, SaveDTO, UpdateDTO>
//        extends SuperController<S, Id, Entity, PageQuery, SaveDTO, UpdateDTO> {
//
//    /**
//     * 查询
//     *
//     * @param id 主键id
//     * @return 查询结果
//     */
//    @Override
//    @SysLog("'查询:' + #id")
//    @PreAuth("hasAnyPermission('{}view')")
//    public R<Entity> get(@PathVariable Id id) {
//        return success(baseService.getByIdCache(id));
//    }
//
//
//    /**
//     * 刷新缓存
//     *
//     * @return 是否成功
//     */
//    @ApiOperation(value = "刷新缓存", notes = "刷新缓存")
//    @PostMapping("refreshCache")
//    @SysLog("'刷新缓存'")
//    @PreAuth("hasAnyPermission('{}add')")
//    public R<Boolean> refreshCache() {
//        baseService.refreshCache();
//        return success(true);
//    }
//
//    /**
//     * 清理缓存
//     *
//     * @return 是否成功
//     */
//    @ApiOperation(value = "清理缓存", notes = "清理缓存")
//    @PostMapping("clearCache")
//    @SysLog("'清理缓存'")
//    @PreAuth("hasAnyPermission('{}add')")
//    public R<Boolean> clearCache() {
//        baseService.clearCache();
//        return success(true);
//    }
//}
