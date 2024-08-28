package com.taotao.cloud.iot.biz.convert;

import net.maku.iot.entity.IotDeviceEntity;
import net.maku.iot.vo.IotDeviceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 设备表
 *
 * @author LSF maku_lsf@163.com
 */
@Mapper
public interface IotDeviceConvert {
    IotDeviceConvert INSTANCE = Mappers.getMapper(IotDeviceConvert.class);

    IotDeviceEntity convert(IotDeviceVO vo);

    IotDeviceVO convert(IotDeviceEntity entity);

    List<IotDeviceVO> convertList(List<IotDeviceEntity> list);

}
