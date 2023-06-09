/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Cloud licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.strategy;

import com.taotao.cloud.security.springsecurity.core.definition.domain.HerodotusPermission;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 本地权限服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/4/1 18:56
 */
public class HerodotusLocalPermissionDetailsService extends AbstractStrategyPermissionDetailsService {

//    private final SysPermissionService sysPermissionService;
//
//    public HerodotusLocalPermissionDetailsService(SysPermissionService sysPermissionService) {
//        this.sysPermissionService = sysPermissionService;
//    }

    @Override
    public List<HerodotusPermission> findAll() {
//        List<SysPermission> authorities = sysPermissionService.findAll();;
//        if (CollectionUtils.isNotEmpty(authorities)) {
//            return toEntities(authorities);
//        }

        return new ArrayList<>();
    }
}
