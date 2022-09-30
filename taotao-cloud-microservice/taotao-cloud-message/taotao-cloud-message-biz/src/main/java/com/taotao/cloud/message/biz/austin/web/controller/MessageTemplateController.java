package com.taotao.cloud.message.biz.austin.web.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.message.biz.austin.api.domain.MessageParam;
import com.taotao.cloud.message.biz.austin.api.domain.SendRequest;
import com.taotao.cloud.message.biz.austin.api.domain.SendResponse;
import com.taotao.cloud.message.biz.austin.api.enums.BusinessCode;
import com.taotao.cloud.message.biz.austin.api.service.RecallService;
import com.taotao.cloud.message.biz.austin.api.service.SendService;
import com.taotao.cloud.message.biz.austin.common.enums.RespStatusEnum;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import com.taotao.cloud.message.biz.austin.web.service.MessageTemplateService;
import com.taotao.cloud.message.biz.austin.web.utils.ConvertMap;
import com.taotao.cloud.message.biz.austin.web.vo.MessageTemplateParam;
import com.taotao.cloud.message.biz.austin.web.vo.MessageTemplateVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 消息模板管理Controller
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/messageTemplate")
@Tag(name = "pc端-发送消息API", description = "pc端-发送消息API")
public class MessageTemplateController {

	@Autowired
	private MessageTemplateService messageTemplateService;

	@Autowired
	private SendService sendService;

	@Autowired
	private RecallService recallService;

	@Value("${austin.business.upload.crowd.path}")
	private String dataPath;

	/**
	 * 如果Id存在，则修改
	 * 如果Id不存在，则保存
	 */
	@Operation(summary = "保存数据", description = "保存数据")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/save")
	public BasicResultVO saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
		MessageTemplate info = messageTemplateService.saveOrUpdate(messageTemplate);
		return BasicResultVO.success(info);
	}

	/**
	 * 列表数据
	 */
	@Operation(summary = "列表页", description = "列表页")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/list")
	public BasicResultVO queryList(MessageTemplateParam messageTemplateParam) {
		List<Map<String, Object>> result = ConvertMap.flatList(messageTemplateService.queryList(messageTemplateParam));

		long count = messageTemplateService.count();
		MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder().count(count).rows(result).build();
		return BasicResultVO.success(messageTemplateVo);
	}

	/**
	 * 根据Id查找
	 */
	@Operation(summary = "根据Id查找", description = "根据Id查找")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("query/{id}")
	public BasicResultVO queryById(@PathVariable("id") Long id) {
		Map<String, Object> result = ConvertMap.flatSingle(messageTemplateService.queryById(id));
		return BasicResultVO.success(result);
	}

	/**
	 * 根据Id复制
	 */
	@Operation(summary = "根据Id复制", description = "根据Id复制")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("copy/{id}")
	public BasicResultVO copyById(@PathVariable("id") Long id) {
		messageTemplateService.copy(id);
		return BasicResultVO.success();
	}


	/**
	 * 根据Id删除
	 * id多个用逗号分隔开
	 */
	@Operation(summary = "根据Ids删除", description = "根据Ids删除")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("delete/{id}")
	public BasicResultVO deleteByIds(@PathVariable("id") String id) {
		if (StrUtil.isNotBlank(id)) {
			List<Long> idList = Arrays.stream(id.split(StrUtil.COMMA)).map(s -> Long.valueOf(s)).collect(Collectors.toList());
			messageTemplateService.deleteByIds(idList);
			return BasicResultVO.success();
		}
		return BasicResultVO.fail();
	}


	/**
	 * 测试发送接口
	 */
	@Operation(summary = "测试发送接口", description = "测试发送接口")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("test")
	public BasicResultVO test(@RequestBody MessageTemplateParam messageTemplateParam) {

		Map<String, String> variables = JSON.parseObject(messageTemplateParam.getMsgContent(), Map.class);
		MessageParam messageParam = MessageParam.builder().receiver(messageTemplateParam.getReceiver()).variables(variables).build();
		SendRequest sendRequest = SendRequest.builder().code(BusinessCode.COMMON_SEND.getCode()).messageTemplateId(messageTemplateParam.getId()).messageParam(messageParam).build();
		SendResponse response = sendService.send(sendRequest);
		if (response.getCode() != RespStatusEnum.SUCCESS.getCode()) {
			return BasicResultVO.fail(response.getMsg());
		}
		return BasicResultVO.success(response);
	}

	/**
	 * 撤回接口
	 */
	@Operation(summary = "撤回消息接口", description = "撤回消息接口")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("recall/{id}")
	public BasicResultVO recall(@PathVariable("id") String id) {

		SendRequest sendRequest = SendRequest.builder().code(BusinessCode.RECALL.getCode()).
			messageTemplateId(Long.valueOf(id)).build();
		SendResponse response = recallService.recall(sendRequest);
		if (response.getCode() != RespStatusEnum.SUCCESS.getCode()) {
			return BasicResultVO.fail(response.getMsg());
		}
		return BasicResultVO.success(response);
	}


	/**
	 * 启动模板的定时任务
	 */
	@Operation(summary = "启动模板的定时任务", description = "启动模板的定时任务")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("start/{id}")
	public BasicResultVO start(@RequestBody @PathVariable("id") Long id) {
		return messageTemplateService.startCronTask(id);
	}

	/**
	 * 暂停模板的定时任务
	 */
	@Operation(summary = "暂停模板的定时任务", description = "暂停模板的定时任务")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("stop/{id}")
	public BasicResultVO stop(@RequestBody @PathVariable("id") Long id) {
		return messageTemplateService.stopCronTask(id);
	}

	/**
	 * 上传人群文件
	 */
	@Operation(summary = "上传人群文件", description = "上传人群文件")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("upload")
	public BasicResultVO upload(@RequestParam("file") MultipartFile file) {
		String filePath = new StringBuilder(dataPath)
			.append(IdUtil.fastSimpleUUID())
			.append(file.getOriginalFilename())
			.toString();
		try {
			File localFile = new File(filePath);
			if (!localFile.exists()) {
				localFile.mkdirs();
			}
			file.transferTo(localFile);


		} catch (Exception e) {
			log.error("MessageTemplateController#upload fail! e:{},params{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(file));
			return BasicResultVO.fail(RespStatusEnum.SERVICE_ERROR);
		}
		return BasicResultVO.success(MapUtil.of(new String[][]{{"value", filePath}}));
	}

}

