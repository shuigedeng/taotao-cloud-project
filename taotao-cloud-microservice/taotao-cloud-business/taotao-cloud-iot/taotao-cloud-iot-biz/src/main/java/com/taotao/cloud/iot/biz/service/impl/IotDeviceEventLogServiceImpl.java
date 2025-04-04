package com.taotao.cloud.iot.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.*;
import com.taotao.cloud.iot.biz.convert.IotDeviceEventLogConvert;
import com.taotao.cloud.iot.biz.dao.IotDeviceEventLogDao;
import com.taotao.cloud.iot.biz.entity.IotDeviceEventLogEntity;
import com.taotao.cloud.iot.biz.enums.DeviceEventTypeEnum;
import com.taotao.cloud.iot.biz.query.IotDeviceEventLogQuery;
import com.taotao.cloud.iot.biz.service.IotDeviceEventLogService;
import com.taotao.cloud.iot.biz.vo.IotDeviceEventLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备事件日志
 *
 * @author 
 */
@Service
@AllArgsConstructor
public class IotDeviceEventLogServiceImpl extends BaseServiceImpl<IotDeviceEventLogDao, IotDeviceEventLogEntity> implements IotDeviceEventLogService {

    @Override
    public PageResult<IotDeviceEventLogVO> page(IotDeviceEventLogQuery query) {
        IPage<IotDeviceEventLogEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        List<IotDeviceEventLogVO> vos = IotDeviceEventLogConvert.INSTANCE.convertList(page.getRecords());
        vos.forEach(vo -> {
            vo.setEventTypeEnum(DeviceEventTypeEnum.getEnum(vo.getEventType()));
        });
        return new PageResult<>(vos, page.getTotal());
    }

    private LambdaQueryWrapper<IotDeviceEventLogEntity> getWrapper(IotDeviceEventLogQuery query) {
        LambdaQueryWrapper<IotDeviceEventLogEntity> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(query.getEventTypeEnum())) {
            wrapper.eq(IotDeviceEventLogEntity::getEventType, DeviceEventTypeEnum.parse(query.getEventTypeEnum()).getValue());
        }
        wrapper.eq(query.getDeviceId() != null, IotDeviceEventLogEntity::getDeviceId, query.getDeviceId());
        wrapper.orderByDesc(IotDeviceEventLogEntity::getEventTime);
        return wrapper;
    }

    @Override
    public void save(IotDeviceEventLogVO vo) {
        IotDeviceEventLogEntity entity = IotDeviceEventLogConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(IotDeviceEventLogVO vo) {
        IotDeviceEventLogEntity entity = IotDeviceEventLogConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

    @Override
    public IotDeviceEventLogEntity createDeviceEvent(Long deviceId, Long tenantId, DeviceEventTypeEnum eventType, String eventUid, Object payload) {
        IotDeviceEventLogEntity deviceEvent = new IotDeviceEventLogEntity();
        deviceEvent.setDeviceId(deviceId);
        deviceEvent.setTenantId(tenantId);
        deviceEvent.setEventType(eventType.getValue());
        deviceEvent.setEventUid(eventUid);
        if (payload != null) {
            deviceEvent.setEventPayload((payload instanceof String)
                    ? (String) payload
                    : JsonUtils.toJsonString(payload));
        }
        deviceEvent.setEventTime(LocalDateTime.now());
        return deviceEvent;

    }

    @Override
    public void createAndSaveDeviceEvent(Long deviceId, Long tenantId, DeviceEventTypeEnum eventType, String eventUid, Object payload) {
        save(createDeviceEvent(deviceId, tenantId, eventType, eventUid, payload));
    }

}
