package com.taotao.cloud.message.biz.austin.web.controller;

import com.taotao.cloud.web.request.annotation.RequestLogger;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.web.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 素材管理接口
 *
 * @author 3y
 */
@Slf4j
@RestController
@RequestMapping("/material")
@Tag(name = "pc端-素材管理接口API", description = "pc端-素材管理接口API")
public class MaterialController {


	@Autowired
	private MaterialService materialService;


	/**
	 * 素材上传接口
	 *
	 * @param file        文件内容
	 * @param sendAccount 发送账号
	 * @param sendChannel 发送渠道
	 * @param fileType    文件类型
	 * @return
	 */
	@Operation(summary = "素材上传接口", description = "素材上传接口")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping("/upload")
	public BasicResultVO uploadMaterial(@RequestParam("file") MultipartFile file, String sendAccount, Integer sendChannel, String fileType) {
		if (ChannelType.DING_DING_WORK_NOTICE.getCode().equals(sendChannel)) {
			return materialService.dingDingMaterialUpload(file, sendAccount, fileType);
		}
		return BasicResultVO.success();
	}

}
