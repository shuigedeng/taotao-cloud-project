package com.taotao.cloud.operation.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.operation.biz.entity.Special;

/**
 * 专题活动业务层
 */
public interface SpecialService extends IService<Special> {

    /**
     * 添加专题活动
     * @param special 专题活动
     * @return 专题活动
     */
    Special addSpecial(Special special);

    /**
     * 删除专题活动
     * @param id 活动ID
     * @return 操作状态
     */
    boolean removeSpecial(String id);

}
