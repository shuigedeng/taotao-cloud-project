package com.taotao.cloud.iot.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import com.taotao.cloud.iot.biz.convert.IotDeviceConvert;
import com.taotao.cloud.iot.biz.entity.IotDeviceEntity;
import com.taotao.cloud.iot.biz.query.IotDeviceQuery;
import com.taotao.cloud.iot.biz.service.IotDeviceService;
import com.taotao.cloud.iot.biz.vo.DeviceCommandVO;
import com.taotao.cloud.iot.biz.vo.DeviceReportAttributeDataVO;
import com.taotao.cloud.iot.biz.vo.IotDeviceVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备表
 *
 * @author 
 */
@RestController
@RequestMapping("iot/device")
@Tag(name = "设备表")
@AllArgsConstructor
public class IotDeviceController {
    private final IotDeviceService iotDeviceService;

    @GetMapping("/page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('iot:device:page')")
    public Result<PageResult<IotDeviceVO>> page(@ParameterObject @Valid IotDeviceQuery query) {
        PageResult<IotDeviceVO> page = iotDeviceService.page(query);

        return Result.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('iot:device:info')")
    public Result<IotDeviceVO> get(@PathVariable("id") Long id) {
        IotDeviceEntity entity = iotDeviceService.getById(id);

        return Result.ok(IotDeviceConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('iot:device:save')")
    public Result<String> save(@RequestBody IotDeviceVO vo) {
        iotDeviceService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('iot:device:update')")
    public Result<String> update(@RequestBody @Valid IotDeviceVO vo) {
        iotDeviceService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('iot:device:delete')")
    public Result<String> delete(@RequestBody List<Long> idList) {
        iotDeviceService.delete(idList);

        return Result.ok();
    }

    @PostMapping("/asyncSendCommand")
    @Operation(summary = "下发指令-不等待设备回复")
    @PreAuthorize("hasAuthority('iot:device:send')")
    public Result<String> asyncSendCommand(@RequestBody DeviceCommandVO vo) {
        iotDeviceService.asyncSendCommand(vo);
        return Result.ok();
    }

    @PostMapping("/syncSendCommand")
    @Operation(summary = "下发指令-等待设备回复")
    @PreAuthorize("hasAuthority('iot:device:send')")
    public Result<String> syncSendCommand(@RequestBody DeviceCommandVO vo) {
        iotDeviceService.syncSendCommand(vo);
        return Result.ok();
    }

    @PostMapping("/syncSendCommand/debug")
    @Operation(summary = "下发指令-等待设备回复-调试")
    @PreAuthorize("hasAuthority('iot:device:send')")
    public Result<String> syncSendCommandDebug(@RequestBody DeviceCommandVO vo) {
        return Result.ok(iotDeviceService.syncSendCommandDebug(vo).getResponsePayload());
    }

    @PostMapping("/simulateDeviceReportAttributeData")
    @Operation(summary = "模拟设备属性数据上报")
    @PreAuthorize("hasAuthority('iot:device:report')")
    public Result<String> simulateDeviceReportAttributeData(@RequestBody DeviceReportAttributeDataVO vo) {
        iotDeviceService.simulateDeviceReportAttributeData(vo);
        return Result.ok();
    }

}
