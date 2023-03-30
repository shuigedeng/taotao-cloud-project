/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.workflow.api.vo;

import java.util.Date;
import lombok.Data;

@Data
public class OrganizeAdministratorEntity {

    private String id;

    private String userId;

    private String organizeId;

    private String organizeType;

    private Integer thisLayerAdd;

    private Integer thisLayerEdit;

    private Integer thisLayerDelete;

    private Integer subLayerAdd;

    private Integer subLayerEdit;

    private Integer subLayerDelete;

    private String description;

    private Long sortCode;

    private Integer enabledMark;

    private Date creatorTime;

    private String creatorUserId;

    private Date lastModifyTime;

    private String lastModifyUserId;

    private Integer deleteMark;

    private Date deleteTime;

    private String deleteUserId;
}
