package com.taotao.cloud.order.biz.controller.manager;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.common.vo.SearchVO;
import cn.lili.modules.payment.entity.RefundLog;
import cn.lili.modules.payment.service.RefundLogService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,退款日志接口
 *
 * 
 * @since 2020/11/16 10:07 下午
 */
@Validated
@RestController
@RequestMapping("/sys/manager/dept")
@Tag(name = "平台管理端-部门管理API", description = "平台管理端-部门管理API")

@RestController
@Api(tags = "管理端,退款日志接口")
@RequestMapping("/order/manager/refundLog")
@Transactional(rollbackFor = Exception.class)
public class RefundLogController {
    @Autowired
    private RefundLogService refundLogService;
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查看退款日志详情")
    public ResultMessage<RefundLog> get(@PathVariable String id) {
        return ResultUtil.data(refundLogService.getById(id));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @GetMapping
    @ApiOperation(value = "分页获取退款日志")
    public ResultMessage<IPage<RefundLog>> getByPage(RefundLog entity, SearchVO searchVo, PageVO page) {
        return ResultUtil.data(refundLogService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo)));
    }

}
