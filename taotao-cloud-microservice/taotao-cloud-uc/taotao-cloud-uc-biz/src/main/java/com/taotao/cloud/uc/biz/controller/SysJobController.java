package com.taotao.cloud.uc.biz.controller;

import com.taotao.cloud.uc.biz.service.ISysJobService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 岗位管理API
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/job")
@Tag(name = "岗位管理API", description = "岗位管理API")
public class SysJobController {

	private final ISysJobService sysJobService;

}
