package com.taotao.cloud.payment.biz.bootx.controller;

import cn.hutool.db.PageResult;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.service.WeChatPayConfigService;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.wechat.WeChatPayConfigDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @author xxm
* @date 2021/3/19
*/
@Tag(name ="微信支付")
@RestController
@RequestMapping("/wechat/pay")
@AllArgsConstructor
public class WeChatPayConfigController {
    private final WeChatPayConfigService weChatPayConfigService;

    @Operation(summary = "添加微信支付配置")
    @PostMapping("/add")
    public ResResult<WeChatPayConfigDto> add(@RequestBody WeChatPayConfigDto param){
        return Res.ok(weChatPayConfigService.add(param));
    }
    
    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<WeChatPayConfigDto> update(@RequestBody WeChatPayConfigDto param){
        return Res.ok(weChatPayConfigService.update(param));
    }
    
    @Operation(summary = "设置启用的微信支付配置")
    @PostMapping("/setUpActivity")
    public ResResult<WeChatPayConfigDto> setUpActivity(Long id){
        weChatPayConfigService.setUpActivity(id);
        return Res.ok();
    }

    @Operation(summary = "清除指定的微信支付配置")
    @PostMapping("/clearActivity")
    public ResResult<Void> clearActivity(Long id){
        weChatPayConfigService.clearActivity(id);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<WeChatPayConfigDto>> page(PageParam pageParam){
        return Res.ok(weChatPayConfigService.page(pageParam));
    }

    @Operation(summary = "根据Id查询")
    @GetMapping("/findById")
    public ResResult<WeChatPayConfigDto> findById(Long id){
        return Res.ok(weChatPayConfigService.findById(id));
    }

    @Operation(summary = "微信支持支付方式")
    @GetMapping("/findPayWayList")
    public ResResult<List<KeyValue>> findPayWayList(){
        return Res.ok(weChatPayConfigService.findPayWayList());
    }
}
