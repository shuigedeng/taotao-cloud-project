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

package com.taotao.cloud.sys.biz.gobrs.service;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.gobrs.async.core.GobrsAsync;
import com.gobrs.async.core.common.domain.AsyncResult;
import com.gobrs.async.core.engine.RuleThermalLoad;
import com.gobrs.async.core.property.RuleConfig;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/**
 * The type Gobrs service.
 *
 * @program: gobrs -async-core @ClassName GobrsService
 * @description:
 * @author: sizegang
 * @create: 2022 -03-28
 */
@Service
public class GobrsService {

    @Autowired(required = false)
    private GobrsAsync gobrsAsync;

    @Autowired(required = false)
    private RuleThermalLoad ruleThermalLoad;

    /** Gobrs async. */
    public void gobrsAsync() {
        gobrsAsync.go("test", () -> new Object());
    }

    /**
     * Gobrs test async result.
     *
     * @return the async result
     */
    public AsyncResult gobrsTest() {
        Map<Class, Object> params = new HashMap<>();
        // params.put(AServiceCondition.class, "1");
        // params.put(CServiceCondition.class, "2");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AsyncResult resp = gobrsAsync.go("general", () -> params);
        stopWatch.stop();
        LogUtils.info("millis: {}",stopWatch.getTotalTimeMillis());
        return resp;
    }

    /** gobrsTest 可以使用 jmeter 一直访问着 然后在浏览器调用 http://localhost:9999/gobrs/updateRule 看规则变更效果 */
    public void updateRule() {
        RuleConfig r = new RuleConfig();
        r.setName("anyConditionGeneral");
        r.setContent("AService->CService->EService->GService; BService->DService->FService->HService;");
        // ruleThermalLoad.load(r);
    }
}
