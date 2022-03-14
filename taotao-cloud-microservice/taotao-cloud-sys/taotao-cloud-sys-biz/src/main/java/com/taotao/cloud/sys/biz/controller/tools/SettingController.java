package com.taotao.cloud.sys.biz.controller.tools;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.netty.annotation.RequestParam;
import com.taotao.cloud.security.annotation.NotAuth;
import com.taotao.cloud.sys.api.vo.setting.SettingVO;
import com.taotao.cloud.sys.biz.entity.Setting;
import com.taotao.cloud.sys.biz.mapper.ISettingMapper;
import com.taotao.cloud.sys.biz.service.ICronService;
import com.taotao.cloud.sys.biz.service.ISettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CronController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:48:47
 */
@Validated
@RestController
@Tag(name = "工具管理端-配置管理API", description = "工具管理端-配置管理API")
@RequestMapping("/sys/tools/setting")
public class SettingController {

	@Autowired
	private ISettingService settingService;

	@NotAuth
	public Result<SettingVO> getByKey(@RequestParam String key) {
		Setting setting = settingService.get(key);
		return Result.success(BeanUtil.copy(setting,SettingVO.class));
	}

	@NotAuth
	@GetMapping("/all")
	public Result<List<Setting>> getAll() {
		List<Setting> list = settingService.list();

		List<Setting> settings = settingService.im().selectList(new QueryWrapper<>());
		List<Setting> all = settingService.cr().findAll();
		List<Setting> all1 = settingService.ir().findAll();
		return Result.success(list);
	}

	@NotAuth
	@PostMapping("/save")
	public Result<Boolean> save(@RequestBody Setting setting) {

		settingService.im().insert(setting);
		settingService.cr().save(setting);
		settingService.ir().save(setting);

		return Result.success(true);
	}

}
