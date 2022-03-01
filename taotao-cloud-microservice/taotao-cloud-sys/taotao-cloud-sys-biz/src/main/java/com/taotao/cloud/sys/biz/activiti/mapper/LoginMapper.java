package com.taotao.cloud.sys.biz.activiti.mapper;

import boot.spring.po.User;

public interface LoginMapper {
	User getpwdbyname(String name);
}
