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

package com.taotao.cloud.sys.biz.config.aware;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 * 这个接口中只有一个方法afterSingletonsInstantiated，其作用是是
 * 在spring容器管理的所有单例对象（非懒加载对象）初始化完成之后调用的回调接口。其触发时机为postProcessAfterInitialization之后。
 *
 * <p>使用场景：用户可以扩展此接口在对所有单例对象初始化完毕后，做一些后置的业务处理。
 */
public class TestSmartInitializingSingleton implements SmartInitializingSingleton {
    @Override
    public void afterSingletonsInstantiated() {
        LogUtils.info("[TestSmartInitializingSingleton]");
    }
}
