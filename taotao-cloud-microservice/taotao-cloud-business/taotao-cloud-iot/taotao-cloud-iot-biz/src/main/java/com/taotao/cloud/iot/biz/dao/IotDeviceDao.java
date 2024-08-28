package com.taotao.cloud.iot.biz.dao;

import net.maku.framework.mybatis.dao.BaseDao;
import net.maku.iot.entity.IotDeviceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备表
 *
 * @author LSF maku_lsf@163.com
 */
@Mapper
public interface IotDeviceDao extends BaseDao<IotDeviceEntity> {

}
