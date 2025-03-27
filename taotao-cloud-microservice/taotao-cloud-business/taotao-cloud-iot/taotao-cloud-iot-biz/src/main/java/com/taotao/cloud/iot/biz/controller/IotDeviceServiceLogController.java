package com.taotao.cloud.iot.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import com.taotao.cloud.iot.biz.convert.IotDeviceServiceLogConvert;
import com.taotao.cloud.iot.biz.entity.IotDeviceServiceLogEntity;
import com.taotao.cloud.iot.biz.query.IotDeviceServiceLogQuery;
import com.taotao.cloud.iot.biz.service.IotDeviceServiceLogService;
import com.taotao.cloud.iot.biz.vo.IotDeviceServiceLogVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备服务日志
 *
 * @author 
 */
@RestController
@RequestMapping("iot/device_service_log")
@Tag(name = "设备服务日志")
@AllArgsConstructor
public class IotDeviceServiceLogController {
    private final IotDeviceServiceLogService iotDeviceServiceLogService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('iot:device_service_log:page')")
    public Result<PageResult<IotDeviceServiceLogVO>> page(@ParameterObject @Valid IotDeviceServiceLogQuery query) {
        PageResult<IotDeviceServiceLogVO> page = iotDeviceServiceLogService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('iot:device_service_log:info')")
    public Result<IotDeviceServiceLogVO> get(@PathVariable("id") Long id) {
        IotDeviceServiceLogEntity entity = iotDeviceServiceLogService.getById(id);

        return Result.ok(IotDeviceServiceLogConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('iot:device_service_log:save')")
    public Result<String> save(@RequestBody IotDeviceServiceLogVO vo) {
        iotDeviceServiceLogService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('iot:device_service_log:update')")
    public Result<String> update(@RequestBody @Valid IotDeviceServiceLogVO vo) {
        iotDeviceServiceLogService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('iot:device_service_log:delete')")
    public Result<String> delete(@RequestBody List<Long> idList) {
        iotDeviceServiceLogService.delete(idList);

        return Result.ok();
    }
}
