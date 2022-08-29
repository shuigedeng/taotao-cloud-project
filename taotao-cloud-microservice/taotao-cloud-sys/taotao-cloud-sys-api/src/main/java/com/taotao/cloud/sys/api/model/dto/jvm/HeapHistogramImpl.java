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

package com.taotao.cloud.sys.api.model.dto.jvm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class HeapHistogramImpl extends HeapHistogram {
    private static final String BOOLEAN_TEXT = "boolean"; // NOI18N
    private static final String CHAR_TEXT = "char"; // NOI18N
    private static final String BYTE_TEXT = "byte"; // NOI18N
    private static final String SHORT_TEXT = "short"; // NOI18N
    private static final String INT_TEXT = "int"; // NOI18N
    private static final String LONG_TEXT = "long"; // NOI18N
    private static final String FLOAT_TEXT = "float"; // NOI18N
    private static final String DOUBLE_TEXT = "double"; // NOI18N
    private static final String VOID_TEXT = "void"; // NOI18N
    private static final char BOOLEAN_CODE = 'Z'; // NOI18N
    private static final char CHAR_CODE = 'C'; // NOI18N
    private static final char BYTE_CODE = 'B'; // NOI18N
    private static final char SHORT_CODE = 'S'; // NOI18N
    private static final char INT_CODE = 'I'; // NOI18N
    private static final char LONG_CODE = 'J'; // NOI18N
    private static final char FLOAT_CODE = 'F'; // NOI18N
    private static final char DOUBLE_CODE = 'D'; // NOI18N
    private static final char OBJECT_CODE = 'L'; // NOI18N
    List<ClassInfo> classes;
    Date time;
    long totalBytes;
    long totalInstances;
    long totalHeapBytes;
    long totalHeapInstances;

    public HeapHistogramImpl() {
    }

    public HeapHistogramImpl(String histogramText) {
        Map<String,ClassInfoImpl> classesMap = new HashMap<>(1024);
        time = new Date();
        Scanner sc = new Scanner(histogramText);
        sc.useRadix(10);
        while(!sc.hasNext("-+")) {
            sc.nextLine();
        }
        sc.skip("-+");
        sc.nextLine();


        while(sc.hasNext("[0-9]+:")) {  // NOI18N
            ClassInfoImpl newClInfo = new ClassInfoImpl(sc);
            storeClassInfo(newClInfo, classesMap);
            totalHeapBytes += newClInfo.getBytes();
            totalHeapInstances += newClInfo.getInstancesCount();
        }
        sc.next("Total");   // NOI18N
        totalInstances = sc.nextLong();
        totalBytes = sc.nextLong();
        classes = new ArrayList<>(classesMap.values());
    }

    void storeClassInfo(final ClassInfoImpl newClInfo, final Map<String, ClassInfoImpl> map) {
        ClassInfoImpl oldClInfo = map.get(newClInfo.getName());
        if (oldClInfo == null) {
            map.put(newClInfo.getName(),newClInfo);
        } else {
            oldClInfo.bytes += newClInfo.getBytes();
            oldClInfo.instances += newClInfo.getInstancesCount();
        }
    }

    @Override
    public Date getTime() {
        return (Date) time.clone();
    }

    @Override
    public List<ClassInfo> getHeapHistogram() {
        return classes;
    }

    @Override
    public long getTotalInstances() {
        return totalInstances;
    }

    @Override
    public long getTotalBytes() {
        return totalBytes;
    }

    @Override
    public long getTotalHeapInstances() {
        return totalHeapInstances;
    }

    @Override
    public long getTotalHeapBytes() {
        return totalHeapBytes;
    }

    @Override
    public Set<ClassInfo> getPermGenHistogram() {
        return new HashSet<>();
    }

    @Override
    public long getTotalPerGenInstances() {
        return -1;
    }

    @Override
    public long getTotalPermGenHeapBytes() {
        return -1;
    }

    public static class ClassInfoImpl extends ClassInfo {
        public long instances;
        public long bytes;
        public String name;

        public ClassInfoImpl(Scanner sc) {
            String jvmName;

            sc.next();
            instances = sc.nextLong();
            bytes = sc.nextLong();
            jvmName = sc.next();
            sc.nextLine();  // skip module name on JDK 9
            name = convertJVMName(jvmName);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getInstancesCount() {
            return instances;
        }

        @Override
        public long getBytes() {
            return bytes;
        }

        @Override
        public int hashCode() {
            return getName().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ClassInfoImpl) {
                return getName().equals(((ClassInfoImpl)obj).getName());
            }
            return false;
        }

        private String convertJVMName(String jvmName) {
            String className = null;
            int index = jvmName.lastIndexOf('[');     // NOI18N

            if (index != -1) {
                switch(jvmName.charAt(index+1)) {
                    case BOOLEAN_CODE:
                        className=BOOLEAN_TEXT;
                        break;
                    case CHAR_CODE:
                        className=CHAR_TEXT;
                        break;
                    case BYTE_CODE:
                        className=BYTE_TEXT;
                        break;
                    case SHORT_CODE:
                        className=SHORT_TEXT;
                        break;
                    case INT_CODE:
                        className=INT_TEXT;
                        break;
                    case LONG_CODE:
                        className=LONG_TEXT;
                        break;
                    case FLOAT_CODE:
                        className=FLOAT_TEXT;
                        break;
                    case DOUBLE_CODE:
                        className=DOUBLE_TEXT;
                        break;
                    case OBJECT_CODE:
                        className=jvmName.substring(index+2,jvmName.length()-1);
                        break;
                    default:
                        System.err.println("Uknown name "+jvmName);     // NOI18N
                        className = jvmName;
                }
                for (int i=0;i<=index;i++) {
                    className+="[]";
                }
            }
            if (className == null) {
                className = jvmName;
            }
            return className.intern();
        }
    }
}
