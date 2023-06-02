/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.message.biz.austin.web.controller;

import org.dromara.hutoolcore.map.MapUtil;
import org.dromara.hutoolcore.util.IdUtil;
import org.dromara.hutoolcore.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.api.domain.MessageParam;
import com.taotao.cloud.message.biz.austin.api.domain.SendRequest;
import com.taotao.cloud.message.biz.austin.api.domain.SendResponse;
import com.taotao.cloud.message.biz.austin.api.enums.BusinessCode;
import com.taotao.cloud.message.biz.austin.api.service.RecallService;
import com.taotao.cloud.message.biz.austin.api.service.SendService;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import com.taotao.cloud.message.biz.austin.web.annotation.AustinResult;
import com.taotao.cloud.message.biz.austin.web.exception.CommonException;
import com.taotao.cloud.message.biz.austin.web.service.MessageTemplateService;
import com.taotao.cloud.message.biz.austin.web.utils.Convert4Amis;
import com.taotao.cloud.message.biz.austin.web.utils.LoginUtils;
import com.taotao.cloud.message.biz.austin.web.vo.MessageTemplateParam;
import com.taotao.cloud.message.biz.austin.web.vo.MessageTemplateVo;
import com.taotao.cloud.message.biz.austin.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 消息模板管理Controller
 *
 * @author 3y
 */
@Slf4j
@AustinResult
@RestController
@RequestMapping("/messageTemplate")
@Api("发送消息")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private SendService sendService;

    @Autowired
    private RecallService recallService;

    @Autowired
    private LoginUtils loginUtils;

    @Value("${austin.business.upload.crowd.path}")
    private String dataPath;

    /** 如果Id存在，则修改 如果Id不存在，则保存 */
    @PostMapping("/save")
    @ApiOperation("/保存数据")
    public MessageTemplate saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        if (loginUtils.needLogin() && StrUtil.isBlank(messageTemplate.getCreator())) {
            throw new CommonException(RespStatusEnum.NO_LOGIN);
        }
        return messageTemplateService.saveOrUpdate(messageTemplate);
    }

    /** 列表数据 */
    @GetMapping("/list")
    @ApiOperation("/列表页")
    public MessageTemplateVo queryList(@Validated MessageTemplateParam messageTemplateParam) {
        if (loginUtils.needLogin() && StrUtil.isBlank(messageTemplateParam.getCreator())) {
            throw new CommonException(RespStatusEnum.NO_LOGIN);
        }
        Page<MessageTemplate> messageTemplates = messageTemplateService.queryList(messageTemplateParam);
        List<Map<String, Object>> result = Convert4Amis.flatListMap(messageTemplates.toList());
        return MessageTemplateVo.builder()
                .count(messageTemplates.getTotalElements())
                .rows(result)
                .build();
    }

    /** 根据Id查找 */
    @GetMapping("query/{id}")
    @ApiOperation("/根据Id查找")
    public Map<String, Object> queryById(@PathVariable("id") Long id) {
        return Convert4Amis.flatSingleMap(messageTemplateService.queryById(id));
    }

    /** 根据Id复制 */
    @PostMapping("copy/{id}")
    @ApiOperation("/根据Id复制")
    public void copyById(@PathVariable("id") Long id) {
        messageTemplateService.copy(id);
    }

    /** 根据Id删除 id多个用逗号分隔开 */
    @DeleteMapping("delete/{id}")
    @ApiOperation("/根据Ids删除")
    public void deleteByIds(@PathVariable("id") String id) {
        if (StrUtil.isNotBlank(id)) {
            List<Long> idList =
                    Arrays.stream(id.split(StrUtil.COMMA)).map(Long::valueOf).toList();
            messageTemplateService.deleteByIds(idList);
        }
    }

    /** 测试发送接口 */
    @PostMapping("test")
    @ApiOperation("/测试发送接口")
    public SendResponse test(@RequestBody MessageTemplateParam messageTemplateParam) {

        Map<String, String> variables = JSON.parseObject(messageTemplateParam.getMsgContent(), Map.class);
        MessageParam messageParam = MessageParam.builder()
                .receiver(messageTemplateParam.getReceiver())
                .variables(variables)
                .build();
        SendRequest sendRequest = SendRequest.builder()
                .code(BusinessCode.COMMON_SEND.getCode())
                .messageTemplateId(messageTemplateParam.getId())
                .messageParam(messageParam)
                .build();
        SendResponse response = sendService.send(sendRequest);
        if (!Objects.equals(response.getCode(), RespStatusEnum.SUCCESS.getCode())) {
            throw new CommonException(response.getMsg());
        }
        return response;
    }

    /** 获取需要测试的模板占位符，透出给Amis */
    @PostMapping("test/content")
    @ApiOperation("/测试发送接口")
    public CommonAmisVo test(Long id) {
        MessageTemplate messageTemplate = messageTemplateService.queryById(id);
        return Convert4Amis.getTestContent(messageTemplate.getMsgContent());
    }

    /** 撤回接口 */
    @PostMapping("recall/{id}")
    @ApiOperation("/撤回消息接口")
    public SendResponse recall(@PathVariable("id") String id) {
        SendRequest sendRequest = SendRequest.builder()
                .code(BusinessCode.RECALL.getCode())
                .messageTemplateId(Long.valueOf(id))
                .build();
        SendResponse response = recallService.recall(sendRequest);
        if (!Objects.equals(response.getCode(), RespStatusEnum.SUCCESS.getCode())) {
            throw new CommonException(response.getMsg());
        }
        return response;
    }

    /** 启动模板的定时任务 */
    @PostMapping("start/{id}")
    @ApiOperation("/启动模板的定时任务")
    public BasicResultVO start(@RequestBody @PathVariable("id") Long id) {
        return messageTemplateService.startCronTask(id);
    }

    /** 暂停模板的定时任务 */
    @PostMapping("stop/{id}")
    @ApiOperation("/暂停模板的定时任务")
    public BasicResultVO stop(@RequestBody @PathVariable("id") Long id) {
        return messageTemplateService.stopCronTask(id);
    }

    /** 上传人群文件 */
    @PostMapping("upload")
    @ApiOperation("/上传人群文件")
    public HashMap<Object, Object> upload(@RequestParam("file") MultipartFile file) {
        String filePath = dataPath + IdUtil.fastSimpleUUID() + file.getOriginalFilename();
        try {
            File localFile = new File(filePath);
            if (!localFile.exists()) {
                localFile.mkdirs();
            }
            file.transferTo(localFile);
        } catch (Exception e) {
            log.error(
                    "MessageTemplateController#upload fail! e:{},params{}",
                    Throwables.getStackTraceAsString(e),
                    JSON.toJSONString(file));
            throw new CommonException(RespStatusEnum.SERVICE_ERROR);
        }
        return MapUtil.of(new String[][] {{"value", filePath}});
    }
}
