package com.taotao.cloud.iot.biz.convert;

import com.taotao.cloud.iot.biz.entity.IotDeviceEventLogEntity;
import com.taotao.cloud.iot.biz.vo.IotDeviceEventLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 设备事件日志
 *
 * @author 
 */
@Mapper
public interface IotDeviceEventLogConvert {
    IotDeviceEventLogConvert INSTANCE = Mappers.getMapper(IotDeviceEventLogConvert.class);

    IotDeviceEventLogEntity convert(IotDeviceEventLogVO vo);

    IotDeviceEventLogVO convert(IotDeviceEventLogEntity entity);

    List<IotDeviceEventLogVO> convertList(List<IotDeviceEventLogEntity> list);

}
