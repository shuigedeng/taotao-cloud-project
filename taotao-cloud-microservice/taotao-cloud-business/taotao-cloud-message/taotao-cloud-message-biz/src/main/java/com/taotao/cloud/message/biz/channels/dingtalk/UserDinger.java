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

package com.taotao.cloud.message.biz.dingtalk; /// *
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
// package com.taotao.cloud.demo.dingtalk;
//
// import com.taotao.cloud.dingtalk.annatations.DingerMarkdown;
// import com.taotao.cloud.dingtalk.annatations.DingerText;
// import com.taotao.cloud.dingtalk.annatations.MultiDinger;
// import com.taotao.cloud.dingtalk.annatations.MultiHandler;
// import com.taotao.cloud.dingtalk.annatations.Parameter;
// import com.taotao.cloud.dingtalk.entity.DingerResponse;
// import com.taotao.cloud.dingtalk.enums.DingerType;
//
/// **
// * UserDinger
// *
// * @author shuigedeng
// * @version 2022.04 v1.0
// * @since 2021/09/03 17:27
// */
// @MultiHandler(
//	@MultiDinger(
//		dinger = DingerType.DINGTALK,
//		handler = UserDingerConfigHandler.class
//	)
// )
// public interface UserDinger {
//
//	@DingerText(value = "taotao 用户${username}注册成功", phones = "111111111111")
//	DingerResponse userRegister(String username);
//
//	@DingerMarkdown(value = "taotao 用户注销通知 ${userId} , ${username}",
//		title = "taotao 测试", phones = "11111111111")
//	DingerResponse userLogout(@Parameter("userId") Long userId, String username);
//
// }
