package com.taotao.cloud.iot.biz.convert;

import net.maku.iot.entity.IotDeviceServiceLogEntity;
import net.maku.iot.vo.IotDeviceServiceLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 设备服务日志
 *
 * @author LSF maku_lsf@163.com
 */
@Mapper
public interface IotDeviceServiceLogConvert {
    IotDeviceServiceLogConvert INSTANCE = Mappers.getMapper(IotDeviceServiceLogConvert.class);

    IotDeviceServiceLogEntity convert(IotDeviceServiceLogVO vo);

    IotDeviceServiceLogVO convert(IotDeviceServiceLogEntity entity);

    List<IotDeviceServiceLogVO> convertList(List<IotDeviceServiceLogEntity> list);

}
