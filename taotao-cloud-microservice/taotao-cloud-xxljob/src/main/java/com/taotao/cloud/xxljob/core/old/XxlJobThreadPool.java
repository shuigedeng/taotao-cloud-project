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

package com.taotao.cloud.xxljob.core.old; // package com.xxl.job.admin.core.quartz;
//
// import org.quartz.SchedulerConfigException;
// import org.quartz.spi.ThreadPool;
//
/// **
// * single thread pool, for async trigger
// *
// * @author xuxueli 2019-03-06
// */
// public class XxlJobThreadPool implements ThreadPool {
//
//    @Override
//    public boolean runInThread(Runnable runnable) {
//
//        // async run
//        runnable.run();
//        return true;
//
//        //return false;
//    }
//
//    @Override
//    public int blockForAvailableThreads() {
//        return 1;
//    }
//
//    @Override
//    public void initialize() throws SchedulerConfigException {
//
//    }
//
//    @Override
//    public void shutdown(boolean waitForJobsToComplete) {
//
//    }
//
//    @Override
//    public int getPoolSize() {
//        return 1;
//    }
//
//    @Override
//    public void setInstanceId(String schedInstId) {
//
//    }
//
//    @Override
//    public void setInstanceName(String schedName) {
//
//    }
//
//    // support
//    public void setThreadCount(int count) {
//        //
//    }
//
// }
