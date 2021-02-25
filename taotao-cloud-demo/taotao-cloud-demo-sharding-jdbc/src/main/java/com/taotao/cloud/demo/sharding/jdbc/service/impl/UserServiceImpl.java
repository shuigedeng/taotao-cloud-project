package com.taotao.cloud.demo.sharding.jdbc.service.impl;

import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
import com.taotao.cloud.demo.sharding.jdbc.mapper.UserMapper;
import com.taotao.cloud.demo.sharding.jdbc.model.User;
import com.taotao.cloud.demo.sharding.jdbc.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zlt
 */
@Slf4j
@Service
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements IUserService {

}
