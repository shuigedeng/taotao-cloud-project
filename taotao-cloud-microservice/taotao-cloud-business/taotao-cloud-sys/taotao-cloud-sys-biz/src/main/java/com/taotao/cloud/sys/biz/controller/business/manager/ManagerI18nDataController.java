package com.taotao.cloud.sys.biz.controller.business.manager;

import com.taotao.cloud.sys.biz.service.business.I18nDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 国际化信息
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/i18n/i18n-data")
@Tag(name = "国际化信息管理")
public class ManagerI18nDataController {

	private final I18nDataService i18nDataService;

//	@GetMapping("/page")
//	@PreAuthorize("@per.hasPermission('i18n:i18n-data:read')")
//	@Operation(summary = "分页查询", description = "分页查询")
//	public Result<PageResult<I18nDataPageVO>> getI18nDataPage(@Validated PageQuery pageParam, I18nDataQO i18nDataQO) {
//		IPage<I18nDataPageVO> page = i18nDataService.queryPage(pageParam, i18nDataQO);
//		return Result.success(MpUtils.convertMybatisPage(page, I18nDataPageVO.class));
//	}
//
//	@GetMapping("/list")
//	@PreAuthorize("@per.hasPermission('i18n:i18n-data:read')")
//	@Operation(summary = "查询指定国际化标识的所有数据", description = "查询指定国际化标识的所有数据")
//	public Result<List<I18nData>> listByCode(@RequestParam("code") String code) {
//		return Result.success(i18nDataService.listByCode(code));
//	}
//
//	@PostMapping
//	@PreAuthorize("@per.hasPermission('i18n:i18n-data:add')")
//	@Operation(summary = "新增国际化信息", description = "新增国际化信息")
//	public Result<Boolean> save(@Valid @RequestBody I18nDataCreateDTO i18nDataCreateDTO) {
//		// 转换为实体类列表
//		List<I18nData> list = new ArrayList<>();
//		List<I18nDataCreateDTO.LanguageText> languageTexts = i18nDataCreateDTO.getLanguageTexts();
//		for (I18nDataCreateDTO.LanguageText languageText : languageTexts) {
//			I18nData i18nData = new I18nData();
//			i18nData.setCode(i18nDataCreateDTO.getCode());
//			i18nData.setRemarks(i18nDataCreateDTO.getRemarks());
//			i18nData.setLanguageTag(languageText.getLanguageTag());
//			i18nData.setMessage(languageText.getMessage());
//			list.add(i18nData);
//		}
////		return i18nDataService.saveBatch(list) ? R.ok() : R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "新增国际化信息失败");
//		return Result.success(true);
//	}
//
//	@PutMapping
//	@PreAuthorize("@per.hasPermission('i18n:i18n-data:edit')")
//	@Operation(summary = "修改国际化信息", description = "修改国际化信息")
//	public Result<Boolean> updateById(@RequestBody I18nDataDTO i18nDataDTO) {
////		return i18nDataService.updateByCodeAndLanguageTag(i18nDataDTO) ? R.ok()
////			: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "修改国际化信息失败");
//		return Result.success(true);
//	}
//
//	@DeleteMapping
//	@PreAuthorize("@per.hasPermission('i18n:i18n-data:del')")
//	@Operation(summary = "通过id删除国际化信息", description = "通过id删除国际化信息")
//	public Result<Boolean> removeById(@RequestParam("code") String code, @RequestParam("languageTag") String languageTag) {
////		return i18nDataService.removeByCodeAndLanguageTag(code, languageTag) ? R.ok()
////			: R.failed(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除国际化信息失败");
//		return Result.success(true);
//	}
//
//	@PostMapping("/import")
//	@PreAuthorize("@per.hasPermission('i18n:i18n-data:import')")
//	@Operation(summary = "导入国际化信息", description = "导入国际化信息")
//	public Result<Boolean> importI18nData(
////		@RequestParam("importMode") ImportModeEnum importModeEnum,
//		@RequestExcel List<I18nDataExcelVO> excelVos
//	) {
//
//		if (CollUtil.isEmpty(excelVos)) {
//			return Result.success(true);
//		}
//
//		// 转换结构
//		List<I18nData> list = excelVos.stream()
//			.map(I18nDataConverter.INSTANCE::excelVoToPo)
//			.toList();
//
//		// 跳过已有数据，返回已有数据列表
////		if (importModeEnum == ImportModeEnum.SKIP_EXISTING) {
////			List<I18nData> existsList = i18nDataService.saveWhenNotExist(list);
////			return Result.success(true);
////		}
////
////		// 覆盖已有数据
////		if (importModeEnum == ImportModeEnum.OVERWRITE_EXISTING) {
////			i18nDataService.saveOrUpdate(list);
////		}
//
//		return Result.success(true);
//	}
//
//	@ResponseExcel(name = "国际化信息", i18nHeader = true)
//	@GetMapping("/export")
//	@PreAuthorize("@per.hasPermission('i18n:i18n-data:export')")
//	@Operation(summary = "导出国际化信息", description = "导出国际化信息")
//	public List<I18nDataExcelVO> exportI18nData(I18nDataQO i18nDataQO) {
//		List<I18nData> list = i18nDataService.queryList(i18nDataQO);
//		if (CollUtil.isEmpty(list)) {
//			return new ArrayList<>();
//		}
//		// 转换为 excel vo 对象
//		return list.stream().map(I18nDataConverter.INSTANCE::poToExcelVo).toList();
//	}
//
//	@ResponseExcel(name = "国际化信息模板", i18nHeader = true)
//	@GetMapping("/excel-template")
//	@PreAuthorize("@per.hasPermission('i18n:i18n-data:import')")
//	@Operation(summary = "国际化信息 Excel 模板", description = "国际化信息 Excel 模板")
//	public List<I18nDataExcelVO> excelTemplate() {
//		List<I18nDataExcelVO> list = new ArrayList<>();
//		list.add(new I18nDataExcelVO());
//		return list;
//	}

}
