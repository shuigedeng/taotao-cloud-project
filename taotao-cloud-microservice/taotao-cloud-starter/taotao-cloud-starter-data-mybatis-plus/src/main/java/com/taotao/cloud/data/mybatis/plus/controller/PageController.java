//package com.taotao.cloud.data.mybatis.plus.controller;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//
///**
// * 分页Controller
// *
// * @param <Entity>    实体
// * @param <PageQuery> 分页参数
// * @author zuihou
// * @date 2020年03月07日22:06:35
// */
//public interface PageController<Entity, PageQuery> extends BaseController<Entity> {
//
//
//    /**
//     * 处理参数
//     *
//     * @param params 分页参数
//     */
//    default void handlerQueryParams(PageParams<PageQuery> params) {
//    }
//
//    /**
//     * 执行查询
//     * <p>
//     * 可以覆盖后重写查询逻辑
//     *
//     * @param params  分页参数
//     * @param page    分页对象
//     * @param defSize 默认查询数
//     */
//    default void query(PageParams<PageQuery> params, IPage<Entity> page, Long defSize) {
//        handlerQueryParams(params);
//
//        if (defSize != null) {
//            page.setSize(defSize);
//        }
//        Entity model = BeanUtil.toBean(params.getModel(), getEntityClass());
//
//        QueryWrap<Entity> wrapper = handlerWrapper(model, params);
//        getBaseService().page(page, wrapper);
//
//        // 处理结果
//        handlerResult(page);
//    }
//
//
//    /**
//     * 处理时间区间，可以覆盖后处理组装查询条件
//     *
//     * @param model  实体类
//     * @param params 分页参数
//     * @return 查询构造器
//     */
//    default QueryWrap<Entity> handlerWrapper(Entity model, PageParams<PageQuery> params) {
//        return Wraps.q(model, params.getExtra(), getEntityClass());
//    }
//
//    /**
//     * 自定义处理返回结果
//     *
//     * @param page 分页对象
//     */
//    default void handlerResult(IPage<Entity> page) {
//        // 调用注入方法
//    }
//
//}
