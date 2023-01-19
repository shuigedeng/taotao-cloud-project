package com.taotao.cloud.workflow.biz.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import jnpf.engine.model.flowengine.FlowPagination;
import jnpf.entity.AppDataEntity;
import jnpf.model.AppDataListAllVO;
import jnpf.model.AppFlowListAllVO;

/**
 * app常用数据
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021-08-08
 */
public interface AppDataService extends IService<AppDataEntity> {

    /**
     * 列表
     *
     * @param type 类型
     * @return
     */
    List<AppDataEntity> getList(String type);

    /**
     * 列表
     *
     * @return
     */
    List<AppDataEntity> getList();

    /**
     * 信息
     *
     * @param objectId 对象主键
     * @return
     */
    AppDataEntity getInfo(String objectId);

    /**
     * 验证名称
     *
     * @param objectId 对象主键
     * @return
     */
    boolean isExistByObjectId(String objectId);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(AppDataEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(AppDataEntity entity);

    /**
     * 删除
     *
     * @param objectId 应用主键
     */
    void delete(String objectId);

    /**
     * 流程所有应用
     *
     * @param pagination
     * @return
     */
    List<AppFlowListAllVO> getFlowList(FlowPagination pagination);

    /**
     * 流程所有应用
     * @param type 类型
     * @return
     */
    List<AppDataListAllVO> getDataList(String type);

}
