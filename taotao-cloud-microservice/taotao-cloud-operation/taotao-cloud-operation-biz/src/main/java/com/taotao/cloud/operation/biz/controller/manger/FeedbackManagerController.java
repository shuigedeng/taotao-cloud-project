package com.taotao.cloud.operation.biz.controller.manger;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,意见反馈接口
 */
@RestController
@Api(tags = "管理端,意见反馈接口")
@RequestMapping("/manager/other/feedback")
public class FeedbackManagerController {

    /**
     * 意见反馈
     */
    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation(value = "查询意见反馈列表")
    @ApiImplicitParam(name = "parentId", value = "父id，顶级为0", required = true, dataType = "String", paramType = "path")
    @GetMapping()
    public Result<IPage<Feedback>> page(PageVO pageVO) {
        return Result.success(feedbackService.page(PageUtil.initPage(pageVO)));
    }

    @ApiOperation(value = "查看意见反馈")
    @ApiImplicitParam(name = "id", value = "意见反馈ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{id}")
    public Result<Feedback> getFeedback(@PathVariable String id) {
        return Result.success(this.feedbackService.getById(id));
    }

}
