package com.taotao.cloud.order.biz.controller.seller;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.order.entity.dos.OrderComplaint;
import cn.lili.modules.order.order.entity.enums.CommunicationOwnerEnum;
import cn.lili.modules.order.order.entity.vo.*;
import cn.lili.modules.order.order.service.OrderComplaintCommunicationService;
import cn.lili.modules.order.order.service.OrderComplaintService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 店铺端,交易投诉接口
 *
 * 
 * @since 2020/12/5
 **/
@Validated
@RestController
@RequestMapping("/sys/manager/dept")
@Tag(name = "店铺端-部门管理API", description = "店铺端-部门管理API")

@RestController
@Api(tags = "店铺端,交易投诉接口")
@RequestMapping("/order/seller/complain")
public class OrderComplaintController {

    /**
     * 交易投诉
     */
    @Autowired
    private OrderComplaintService orderComplaintService;

    /**
     * 投诉沟通
     */
    @Autowired
    private OrderComplaintCommunicationService orderComplaintCommunicationService;
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "投诉单ID", required = true, paramType = "path")
    @GetMapping(value = "/{id}")
    public ResultMessage<OrderComplaintVO> get(@PathVariable String id) {
        return ResultUtil.data(OperationalJudgment.judgment(orderComplaintService.getOrderComplainById(id)));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "分页获取")
    @GetMapping
    public ResultMessage<IPage<OrderComplaint>> get(OrderComplaintSearchParams searchParams, PageVO pageVO) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        searchParams.setStoreId(storeId);
        return ResultUtil.data(orderComplaintService.getOrderComplainByPage(searchParams, pageVO));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "添加交易投诉对话")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "complainId", value = "投诉单ID", required = true, paramType = "query"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, paramType = "query")
    })
    @PostMapping("/communication")
    public ResultMessage<OrderComplaintCommunicationVO> addCommunication(@RequestParam String complainId, @RequestParam String content) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        OrderComplaintCommunicationVO communicationVO = new OrderComplaintCommunicationVO(complainId, content, CommunicationOwnerEnum.STORE.name(), currentUser.getStoreId(), currentUser.getUsername());
        orderComplaintCommunicationService.addCommunication(communicationVO);
        return ResultUtil.success();
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "修改申诉信息")
    @PutMapping
    public ResultMessage<OrderComplaintVO> update(OrderComplaintVO orderComplainVO) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        orderComplainVO.setStoreId(storeId);
        orderComplaintService.updateOrderComplain(orderComplainVO);
        return ResultUtil.data(orderComplainVO);
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "申诉")
    @PutMapping("/appeal")
    public ResultMessage<OrderComplaintVO> appeal(StoreAppealVO storeAppealVO) {
        orderComplaintService.appeal(storeAppealVO);
        return ResultUtil.data(orderComplaintService.getOrderComplainById(storeAppealVO.getOrderComplaintId()));
    }
	@Operation(summary = "获取部门树", description = "获取部门树", method = CommonConstant.GET)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
    @ApiOperation(value = "修改状态")
    @PutMapping(value = "/status")
    public ResultMessage<Object> updateStatus(OrderComplaintOperationParams orderComplainVO) {
        orderComplaintService.updateOrderComplainByStatus(orderComplainVO);
        return ResultUtil.success();
    }

}
