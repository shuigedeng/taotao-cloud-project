/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.auth.biz.service;

import com.taotao.cloud.auth.api.dto.ClientDTO;
import com.taotao.cloud.auth.api.query.ClientPageQuery;
import com.taotao.cloud.auth.biz.entity.Client;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * IClientService
 *
 * @author dengtao
 * @date 2020/4/29 15:13
 * @since v1.0
 */
public interface IClientService {

    /**
     * 查询应用列表
     *
     * @param clientPageQuery clientQuery
     * @author dengtao
     * @date 2020/4/29 15:23
     */
    Page<Client> listClient(ClientPageQuery clientPageQuery);

    /**
     * 添加应用
     *
     * @param clientDto clientDto
     * @return com.taotao.cloud.common.model.Result
     * @author dengtao
     * @date 2020/4/29 15:14
     */
    Boolean saveClient(ClientDTO clientDto);

    /**
     * 删除应用
     *
     * @param clientId clientId
     * @return com.taotao.cloud.common.model.Result<java.lang.String>
     * @author dengtao
     * @date 2020/4/29 15:24
     */
    Boolean delByClientId(String clientId);

    /**
     * 根据id获取应用
     *
     * @param clientId clientId
     * @author dengtao
     * @date 2020/8/6 09:51
     */
    Client getByClientId(String clientId);

    /**
     * 获取所有应用
     *
     * @author dengtao
     * @date 2020/10/9 14:48
     * @since v1.0
     */
    List<Client> getAllClient();

    /**
     * 修改应用
     *
     * @param clientId  clientId
     * @param clientDto clientDto
     * @author dengtao
     * @date 2020/10/9 14:56
     * @since v1.0
     */
    Boolean updateClient(String clientId, ClientDTO clientDto);
}
