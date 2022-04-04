package com.taotao.cloud.payment.biz.controller.manger;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import com.taotao.cloud.payment.biz.service.RefundLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,退款日志接口
 */
@RestController
@Api(tags = "管理端,退款日志接口")
@RequestMapping("/manager/order/refundLog")
public class RefundLogManagerController {
    @Autowired
    private RefundLogService refundLogService;

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查看退款日志详情")
    public Result<RefundLog> get(@PathVariable String id) {
        return Result.success(refundLogService.getById(id));
    }

    @GetMapping
    @ApiOperation(value = "分页获取退款日志")
    public Result<IPage<RefundLog>> getByPage(RefundLog entity, SearchVO searchVo, PageVO page) {
        return Result.success(refundLogService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo)));
    }

}
