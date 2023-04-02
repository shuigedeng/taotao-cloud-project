package com.taotao.cloud.sys.biz.sensitive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daffodil.core.entity.JsonResult;
import com.daffodil.core.entity.Page;
import com.daffodil.core.entity.TableResult;
import com.daffodil.framework.annotation.AuthPermission;
import com.daffodil.framework.annotation.OperBusiness.Business;
import com.daffodil.framework.annotation.OperLog;
import com.daffodil.framework.controller.ReactiveBaseController;
import com.daffodil.sensitive.constant.SensitiveConstant;
import com.daffodil.sensitive.controller.model.FilterText;
import com.daffodil.sensitive.controller.model.FilterWords;
import com.daffodil.sensitive.service.ISysSensitiveWordsService;
import com.daffodil.sensitive.entity.SysSensitiveWords;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;
import reactor.core.publisher.Mono;

/**
 * -敏感词词语控制层
 * @author yweijian
 * @date 2022-09-16
 * @version 1.0
 * @description
 */
@Api(value = "敏感词词语管理", tags = "敏感词词语管理")
@RestController
@RequestMapping(SensitiveConstant.API_CONTENT_PATH)
public class SysSensitiveWordsController extends ReactiveBaseController {
    
    @Autowired
    private ISysSensitiveWordsService sensitiveWordsService;

    @ApiOperation("分页查询敏感词词语列表")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "登录授权令牌 Bearer token", paramType = "header", required = true)
    @SuppressWarnings("unchecked")
    @AuthPermission("sensitive:words:list")
    @GetMapping("/words/list")
    public Mono<TableResult> list(SysSensitiveWords words, Page page, @ApiIgnore ServerHttpRequest request) {
        initQuery(words, page, request);
        List<SysSensitiveWords> list = sensitiveWordsService.selectSensitiveWordsList(query);
        return Mono.just(TableResult.success(list, query));
    }
    
    @ApiOperation("获取敏感词词语详情")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "登录授权令牌 Bearer token", paramType = "header", required = true)
    @GetMapping("/words/info")
    public Mono<JsonResult> info(@ApiParam(value = "敏感词词语ID") String id, @ApiIgnore ServerHttpRequest request) {
        SysSensitiveWords word = sensitiveWordsService.selectSensitiveWordsById(id);
        return Mono.just(JsonResult.success(word));
    }

    @ApiOperation("新增敏感词词语")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "登录授权令牌 Bearer token", paramType = "header", required = true)
    @AuthPermission("sensitive:words:add")
    @OperLog(title = "敏感词词语管理", type = Business.INSERT)
    @PostMapping("/words/add")
    public Mono<JsonResult> add(@Validated @RequestBody SysSensitiveWords words, @ApiIgnore ServerHttpRequest request) {
        sensitiveWordsService.insertSensitiveWords(words);
        return Mono.just(JsonResult.success());
    }

    @ApiOperation("修改敏感词词语")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "登录授权令牌 Bearer token", paramType = "header", required = true)
    @AuthPermission("sensitive:words:edit")
    @OperLog(title = "敏感词词语管理", type = Business.UPDATE)
    @PostMapping("/words/edit")
    public Mono<JsonResult> edit(@Validated @RequestBody SysSensitiveWords words, @ApiIgnore ServerHttpRequest request) {
        sensitiveWordsService.updateSensitiveWords(words);
        return Mono.just(JsonResult.success());
    }
    
    @ApiOperation("删除敏感词词语")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "登录授权令牌 Bearer token", paramType = "header", required = true)
    @AuthPermission("sensitive:words:remove")
    @OperLog(title = "敏感词词语管理", type = Business.DELETE)
    @PostMapping("/words/remove")
    public Mono<JsonResult> remove(@RequestBody String[] ids, @ApiIgnore ServerHttpRequest request) {
        sensitiveWordsService.deleteSensitiveWordsByIds(ids);
        return Mono.just(JsonResult.success());
    }
    
    @ApiOperation("检测敏感词词语")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "登录授权令牌 Bearer token", paramType = "header", required = true)
    @PostMapping("/words/check")
    public Mono<JsonResult> check(@RequestBody FilterText filterText, @ApiIgnore ServerHttpRequest request) {
        FilterWords filterWords = sensitiveWordsService.checkSensitiveWords(filterText);
        if(filterWords.getIsMatched()) {
            String errorMsg = "不允许使用敏感词词语【" + filterWords.getWord(0) + "】";
            return Mono.just(new JsonResult(HttpStatus.BAD_REQUEST.value(), errorMsg, filterWords.getWord(0)));
        }
        return Mono.just(JsonResult.success());
    }
    
    @ApiOperation("匹配敏感词词语")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "登录授权令牌 Bearer token", paramType = "header", required = true)
    @PostMapping("/words/match")
    public Mono<JsonResult> match(@RequestBody FilterText filterText, @ApiIgnore ServerHttpRequest request) {
        FilterWords filterWords = sensitiveWordsService.matchSensitiveWords(filterText);
        return Mono.just(JsonResult.success(filterWords));
    }

    @ApiOperation("治理敏感词词语")
    @ApiImplicitParam(name = HttpHeaders.AUTHORIZATION, value = "登录授权令牌 Bearer token", paramType = "header", required = true)
    @PostMapping("/words/mask")
    public Mono<JsonResult> mask(@RequestBody FilterText filterText, @ApiIgnore ServerHttpRequest request){
        String text = sensitiveWordsService.maskSensitiveWords(filterText);
        return Mono.just(JsonResult.success("操作成功", text));
    }
}
