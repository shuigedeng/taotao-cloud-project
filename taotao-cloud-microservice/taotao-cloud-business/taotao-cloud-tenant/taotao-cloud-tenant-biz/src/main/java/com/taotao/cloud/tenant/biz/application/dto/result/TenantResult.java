package com.taotao.cloud.tenant.biz.application.dto.result;

import com.taotao.boot.common.model.ddd.types.MarkerResult;
import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * AppResult
 *
 * @author shuigedeng
 * @version 2026.04
 * @since 2025-12-19 09:30:45
 */
@RecordBuilder
public record TenantResult() implements MarkerResult {}
