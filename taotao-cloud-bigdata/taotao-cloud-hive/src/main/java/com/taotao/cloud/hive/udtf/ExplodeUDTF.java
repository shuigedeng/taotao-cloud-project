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

package com.taotao.cloud.hive.udtf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.hive.ql.exec.TaskExecutionException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.MapObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;

/**
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 17:25
 */
public class ExplodeUDTF extends GenericUDTF {

    private transient ObjectInspector inputOI = null;

	static void main() {

	}
    /**
     * 初始化 构建一个StructObjectInspector类型用于输出 其中struct的字段构成输出的一行
     * <p/>
     * 字段名称不重要，因为它们将被用户提供的列别名覆盖
     *
     * @param argOIs
     * @return
     * @throws UDFArgumentException
     */
    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs)
            throws UDFArgumentException {
        // 得到结构体的字段
        List<? extends StructField> inputFields = argOIs.getAllStructFieldRefs();
        ObjectInspector[] udfInputOIs = new ObjectInspector[inputFields.size()];
        for (int i = 0; i < inputFields.size(); i++) {
            // 字段类型
            udfInputOIs[i] = inputFields.get(i).getFieldObjectInspector();
        }

        if (udfInputOIs.length != 1) {
            throw new UDFArgumentLengthException("explode() takes only one argument");
        }

        List<String> fieldNames = new ArrayList<>();
        List<ObjectInspector> fieldOIs = new ArrayList<>();
        switch (udfInputOIs[0].getCategory()) {
            case LIST:
                inputOI = udfInputOIs[0];
                // 指定list生成的列名，可在as后覆写
                fieldNames.add("col");
                // 获取list元素的类型
                fieldOIs.add(((ListObjectInspector) inputOI).getListElementObjectInspector());
                break;
            case MAP:
                inputOI = udfInputOIs[0];
                // 指定map中key的生成的列名，可在as后覆写
                fieldNames.add("key");
                // 指定map中value的生成的列名，可在as后覆写
                fieldNames.add("value");
                // 得到map中key的类型
                fieldOIs.add(((MapObjectInspector) inputOI).getMapKeyObjectInspector());
                // 得到map中value的类型
                fieldOIs.add(((MapObjectInspector) inputOI).getMapValueObjectInspector());
                break;
            default:
                throw new UDFArgumentException("explode() takes an array or a map as a parameter");
        }
        // 创建一个Struct类型返回
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    // 输出list
    private transient Object[] forwardListObj = new Object[1];
    // 输出map
    private transient Object[] forwardMapObj = new Object[2];

    /**
     * 每行执行一次，输入数据args 每调用forward，输出一行
     *
     * @param args
     * @throws HiveException
     */
    @Override
    public void process(Object[] args) throws HiveException {
        switch (inputOI.getCategory()) {
            case LIST:
                ListObjectInspector listOI = (ListObjectInspector) inputOI;
                List<?> list = listOI.getList(args[0]);
                if (list == null) {
                    return;
                }

                // list中每个元素输出一行
                for (Object o : list) {
                    forwardListObj[0] = o;
                    forward(forwardListObj);
                }
                break;
            case MAP:
                MapObjectInspector mapOI = (MapObjectInspector) inputOI;
                Map<?, ?> map = mapOI.getMap(args[0]);
                if (map == null) {
                    return;
                }
                // map中每一对输出一行
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    forwardMapObj[0] = entry.getKey();
                    forwardMapObj[1] = entry.getValue();
                    forward(forwardMapObj);
                }
                break;
            default:
                throw new TaskExecutionException("explode() can only operate on an array or a map");
        }
    }

    @Override
    public void close() throws HiveException {}
}
