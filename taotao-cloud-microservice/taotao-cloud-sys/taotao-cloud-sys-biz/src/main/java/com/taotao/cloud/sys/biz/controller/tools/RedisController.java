package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.vo.redis.RedisVo;
import com.taotao.cloud.sys.biz.service.RedisService;
import com.taotao.cloud.web.idempotent.Idempotent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RedisController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 16:13:03
 */
@Validated
@RestController
@Tag(name = "平台管理端-redis管理API", description = "平台管理端-redis管理API")
@RequestMapping("/sys/tools/redis")
public class RedisController {

	@Autowired
	private RedisService redisService;

	@Operation(summary = "查询Redis缓存", description = "查询Redis缓存", method = CommonConstant.GET)
	@RequestLogger(description = "查询Redis缓存")
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_SELECT')")
	public ResponseEntity getRedis(String key, Pageable pageable) {
		return new ResponseEntity(redisService.findByKey(key, pageable), HttpStatus.OK);
	}

	@Operation(summary = "删除Redis缓存", description = "删除Redis缓存", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除Redis缓存")
	@Idempotent(key = "delete", perFix = "redis")
	@DeleteMapping(value = "/redis")
	@PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
	public ResponseEntity delete(@RequestBody RedisVo resources) {
		redisService.delete(resources.getKey());
		return new ResponseEntity(HttpStatus.OK);
	}

	@Operation(summary = "清空Redis缓存", description = "清空Redis缓存", method = CommonConstant.DELETE)
	@RequestLogger(description = "清空Redis缓存")
	@Idempotent(key = "deleteAll", perFix = "redis")
	@DeleteMapping(value = "/redis/all")
	@PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
	public ResponseEntity deleteAll() {

		redisService.flushdb();
		return new ResponseEntity(HttpStatus.OK);
	}
}
