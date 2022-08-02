package com.taotao.cloud.sys.biz.modules.core.dtos;

import java.util.ArrayList;
import java.util.List;

public abstract class RootTreeResponseDto<T> implements TreeResponseDto<T> {
	protected T origin;
	protected List<TreeResponseDto> childrens = new ArrayList<>();

	public RootTreeResponseDto(T origin) {
		this.origin = origin;
	}

	@Override
	public List<TreeResponseDto> getChildren() {
		return childrens;
	}
}
