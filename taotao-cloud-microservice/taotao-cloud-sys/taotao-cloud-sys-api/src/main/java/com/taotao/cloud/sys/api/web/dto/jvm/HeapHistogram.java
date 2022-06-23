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

package com.taotao.cloud.sys.api.web.dto.jvm;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public abstract class HeapHistogram {

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public abstract Date getTime();
    public abstract long getTotalInstances();
    public abstract long getTotalBytes();
    public abstract List<ClassInfo> getHeapHistogram();
    public abstract long getTotalHeapInstances();
    public abstract long getTotalHeapBytes();
    public abstract Set<ClassInfo> getPermGenHistogram();
    public abstract long getTotalPerGenInstances();
    public abstract long getTotalPermGenHeapBytes();
    
    public static abstract class ClassInfo {
        
        public abstract String getName();
        public abstract long getInstancesCount();
        public abstract long getBytes();
        
        @Override
        public int hashCode() {
            return getName().hashCode();
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ClassInfo) {
                return getName().equals(((ClassInfo)obj).getName());
            }
            return false;
        }
    }

}
