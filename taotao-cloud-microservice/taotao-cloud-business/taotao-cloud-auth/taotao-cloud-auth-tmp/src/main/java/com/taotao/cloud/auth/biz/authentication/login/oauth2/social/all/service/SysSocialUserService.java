/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.service;

import com.taotao.cloud.auth.biz.authentication.login.oauth2.social.all.entity.SysSocialUser;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 社会化登录用户服务 </p>
 *
 * 
 * @date : 2021/5/16 16:29
 */
@Service
public class SysSocialUserService {

//    private final SysSocialUserRepository sysSocialUserRepository;

//    public SysSocialUserService(SysSocialUserRepository sysSocialUserRepository) {
//        this.sysSocialUserRepository = sysSocialUserRepository;
//    }

    public SysSocialUser findByUuidAndSource(String uuid, String source) {
//        return sysSocialUserRepository.findSysSocialUserByUuidAndSource(uuid, source);
		return null;
    }

	public void saveAndFlush(SysSocialUser sysSocialUser) {
	}
}
