package com.taotao.cloud.iot.biz.convert;

import com.taotao.cloud.iot.biz.entity.IotDeviceEntity;
import com.taotao.cloud.iot.biz.vo.IotDeviceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 设备表
 *
 * @author 
 */
@Mapper
public interface IotDeviceConvert {
    IotDeviceConvert INSTANCE = Mappers.getMapper(IotDeviceConvert.class);

    IotDeviceEntity convert(IotDeviceVO vo);

    IotDeviceVO convert(IotDeviceEntity entity);

    List<IotDeviceVO> convertList(List<IotDeviceEntity> list);

}
