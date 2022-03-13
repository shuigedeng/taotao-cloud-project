package com.taotao.cloud.order.biz.controller.manager;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.common.vo.SearchVO;
import cn.lili.modules.order.trade.entity.dos.OrderLog;
import cn.lili.modules.order.trade.service.OrderLogService;
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
 * 管理端,订单日志管理接口
 *
 * 
 * @since 2020/11/17 4:34 下午
 */
@Validated
@RestController
@RequestMapping("/sys/manager/dept")
@Tag(name = "平台管理端-部门管理API", description = "平台管理端-部门管理API")

@RestController
@Transactional(rollbackFor = Exception.class)
@Api(tags = "管理端,订单日志管理接口")
@RequestMapping("/order/manager/orderLog")
public class OrderLogController {
    @Autowired
    private OrderLogService orderLogService;
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @GetMapping(value = "/get/{id}")
    @ApiOperation(value = "通过id获取")
    public ResultMessage<OrderLog> get(@PathVariable String id) {

        return ResultUtil.data(orderLogService.getById(id));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @GetMapping(value = "/getByPage")
    @ApiOperation(value = "分页获取")
    public ResultMessage<IPage<OrderLog>> getByPage(OrderLog entity,
                                                    SearchVO searchVo,
                                                    PageVO page) {
        return ResultUtil.data(orderLogService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo)));
    }

}
