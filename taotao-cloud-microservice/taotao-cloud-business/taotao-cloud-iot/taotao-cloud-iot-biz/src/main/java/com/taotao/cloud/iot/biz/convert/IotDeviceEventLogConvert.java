package com.taotao.cloud.iot.biz.convert;

import net.maku.iot.entity.IotDeviceEventLogEntity;
import net.maku.iot.vo.IotDeviceEventLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 设备事件日志
 *
 * @author LSF maku_lsf@163.com
 */
@Mapper
public interface IotDeviceEventLogConvert {
    IotDeviceEventLogConvert INSTANCE = Mappers.getMapper(IotDeviceEventLogConvert.class);

    IotDeviceEventLogEntity convert(IotDeviceEventLogVO vo);

    IotDeviceEventLogVO convert(IotDeviceEventLogEntity entity);

    List<IotDeviceEventLogVO> convertList(List<IotDeviceEventLogEntity> list);

}
