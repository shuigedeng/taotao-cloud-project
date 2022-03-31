package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.dto.mongo.CollectionDto;
import com.taotao.cloud.sys.api.dto.mongo.MongoQueryParam;
import com.taotao.cloud.sys.biz.service.IMongoService;
import com.taotao.cloud.sys.biz.service.impl.MongoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MongoController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:53:37
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-mongo管理API", description = "工具管理端-mongo管理API")
@RequestMapping("/sys/tools/mongo")
public class MongoController {

	private final IMongoService mongoService;

	@Operation(summary = "查询所有的库", description = "查询所有的库", method = CommonConstant.GET)
	@RequestLogger(description = "导出数据")
	@PreAuthorize("@el.check('admin','log:list')")
	@GetMapping("/database-names")
	public Result<List<String>> databaseNames() {
		return Result.success(mongoService.databaseNames());
	}

	@Operation(summary = "查询某个库里的所有集合", description = "查询某个库里的所有集合", method = CommonConstant.GET)
	@RequestLogger(description = "查询某个库里的所有集合")
	@PreAuthorize("@el.check('admin','log:list')")
	@GetMapping("/collection-names/{databaseName}")
	public Result<List<CollectionDto>> collectionNames(
		@PathVariable("databaseName") String databaseName) {
		return Result.success(mongoService.collectionNames(databaseName));
	}

	@Operation(summary = "分页数据查询", description = "分页数据查询", method = CommonConstant.GET)
	@RequestLogger(description = "分页数据查询")
	@PreAuthorize("@el.check('admin','log:list')")
	@GetMapping("/page")
	public Result<PageModel<String>> queryPage(@Valid MongoQueryParam mongoQueryParam,
		PageQuery pageQuery) {
		return Result.success(mongoService.queryDataPage(mongoQueryParam, pageQuery));
	}
}
