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
// import com.taotao.cloud.dingtalk.model.DingerConfig;
// import com.taotao.cloud.dingtalk.multi.AlgorithmHandler;
// import com.taotao.cloud.dingtalk.multi.DingerConfigHandler;
// import com.taotao.cloud.dingtalk.multi.RoundRobinHandler;
// import java.util.ArrayList;
// import java.util.List;
//
/// **
// * GlobalDingerConfigHandler
// *
// * @author shuigedeng
// * @version 2022.04 1.0.0
// * @since 2021/09/03 17:24
// */
// public class UserDingerConfigHandler implements DingerConfigHandler {
//
//	@Override
//	public List<DingerConfig> dingerConfigs() {
//		List<DingerConfig> configs = new ArrayList<>();
//		String tokenId = System.getenv("DINGDING_TOKEN_ID");
//		String secret = System.getenv("DINGDING_SECRET");
//
//		configs.add(DingerConfig.instance(tokenId, secret));
//		return configs;
//	}
//
//	@Override
//	public Class<? extends AlgorithmHandler> algorithmHandler() {
//		return RoundRobinHandler.class;
//	}
// }
