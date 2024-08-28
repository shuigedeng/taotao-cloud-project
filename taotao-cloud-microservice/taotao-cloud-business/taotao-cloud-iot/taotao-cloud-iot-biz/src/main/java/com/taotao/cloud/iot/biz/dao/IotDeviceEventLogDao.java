package com.taotao.cloud.iot.biz.dao;

import net.maku.framework.mybatis.dao.BaseDao;
import net.maku.iot.entity.IotDeviceEventLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备事件日志
 *
 * @author LSF maku_lsf@163.com
 */
@Mapper
public interface IotDeviceEventLogDao extends BaseDao<IotDeviceEventLogEntity> {

}
