/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.entity;


import com.taotao.cloud.dingtalk.entity.MsgType;
import com.taotao.cloud.dingtalk.enums.DingerType;

/**
 * DingTalk请求体
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:19:49
 */
public class DingTalkMessage extends MsgType {

	public DingTalkMessage() {
		setDingerType(DingerType.DINGTALK);
	}
}
