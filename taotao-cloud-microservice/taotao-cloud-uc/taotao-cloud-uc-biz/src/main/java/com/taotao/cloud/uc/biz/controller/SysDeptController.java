package com.taotao.cloud.uc.biz.controller;

import com.taotao.cloud.uc.biz.service.ISysDeptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门管理API {@link String} {@code String}
 *
 * @author dengtao
 * @version 1.0
 * @since 2020/10/16 15:54:05
 */
@Validated
@RestController
@RequestMapping("/dept")
@Tag(name = "部门管理API", description = "部门管理API")
@AllArgsConstructor
public class SysDeptController {

	private final ISysDeptService deptService;
}
