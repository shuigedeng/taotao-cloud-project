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

package com.taotao.cloud.trino.plugin.udaf.collect_list;

import io.airlift.slice.SizeOf;
import io.airlift.slice.Slice;
import io.airlift.slice.SliceInput;
import io.airlift.slice.SliceOutput;
import io.airlift.slice.Slices;
import java.util.HashMap;
import java.util.Map;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 18:09
 */
public class CollectListStats {

    private static final int INSTANCE_SIZE =
            (int) ClassLayout.parseClass(CollectListStats.class).instanceSize();
    // <id,<key,value>>
    private final Map<Integer, Map<String, Integer>> collectContainer = new HashMap<>();
    private int contentEstimatedSize = 0;
    private int keyByteLen = 0;
    private int keyListLen = 0;

    public CollectListStats() {}

    public CollectListStats(Slice serialized) {
        deserialize(serialized);
    }

    public void addCollectList(Integer id, String key, int value) {
        if (collectContainer.containsKey(id)) {
            Map<String, Integer> tmpMap = collectContainer.get(id);
            if (tmpMap.containsKey(key)) {
                tmpMap.put(key, tmpMap.get(key) + value);
            } else {
                tmpMap.put(key, value);
                contentEstimatedSize += (key.getBytes().length + SizeOf.SIZE_OF_INT);
                keyByteLen += key.getBytes().length;
                keyListLen++;
            }
        } else {
            Map<String, Integer> tmpMap = new HashMap<String, Integer>();
            tmpMap.put(key, value);
            keyByteLen += key.getBytes().length;
            keyListLen++;
            collectContainer.put(id, tmpMap);
            contentEstimatedSize += SizeOf.SIZE_OF_INT;
        }
    }

    // [{id:1,{"aaa":3,"fadf":6},{}]
    public Slice getCollectResult() {
        Slice jsonSlice = null;
        try {
            StringBuilder jsonStr = new StringBuilder();
            jsonStr.append("[");
            int collectLength = collectContainer.entrySet().size();
            for (Map.Entry<Integer, Map<String, Integer>> mapEntry : collectContainer.entrySet()) {
                Integer id = mapEntry.getKey();
                Map<String, Integer> vMap = mapEntry.getValue();
                jsonStr.append("{id:").append(id).append(",{");
                int vLength = vMap.entrySet().size();
                for (Map.Entry<String, Integer> vEntry : vMap.entrySet()) {
                    String key = vEntry.getKey();
                    Integer value = vEntry.getValue();
                    jsonStr.append(key).append(":").append(value);
                    vLength--;
                    if (vLength != 0) {
                        jsonStr.append(",");
                    }
                }
                jsonStr.append("}");
                collectLength--;
                if (collectLength != 0) {
                    jsonStr.append(",");
                }
            }
            jsonStr.append("]");
            jsonSlice = Slices.utf8Slice(jsonStr.toString());
        } catch (Exception e) {
            throw new RuntimeException(e + " ---------- get CollectResult err");
        }
        return jsonSlice;
    }

    public void deserialize(Slice serialized) {
        try {
            SliceInput input = serialized.getInput();
            // 外层map的长度
            int collectStatsEntrySize = input.readInt();
            for (int collectCnt = 0; collectCnt < collectStatsEntrySize; collectCnt++) {

                int id = input.readInt();
                int keyEntrySize = input.readInt();
                for (int idCnt = 0; idCnt < keyEntrySize; idCnt++) {
                    int keyBytesLen = input.readInt();
                    byte[] keyBytes = new byte[keyBytesLen];
                    for (int byteIdx = 0; byteIdx < keyBytesLen; byteIdx++) {
                        keyBytes[byteIdx] = input.readByte();
                    }
                    String key = new String(keyBytes);
                    int value = input.readInt();
                    addCollectList(id, key, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e + " ----- deserialize err");
        }
    }

    public Slice serialize() {
        SliceOutput builder = null;
        int requiredBytes =
                SizeOf.SIZE_OF_INT * 3
                        + keyListLen * SizeOf.SIZE_OF_INT
                        + keyByteLen
                        + keyListLen * SizeOf.SIZE_OF_INT;
        try {
            // 序列化
            builder = Slices.allocate(requiredBytes).getOutput();
            for (Map.Entry<Integer, Map<String, Integer>> entry : collectContainer.entrySet()) {
                // id个数
                builder.appendInt(collectContainer.entrySet().size());
                // id 数值
                builder.appendInt(entry.getKey());
                Map<String, Integer> kMap = entry.getValue();
                builder.appendInt(kMap.entrySet().size());
                for (Map.Entry<String, Integer> vEntry : kMap.entrySet()) {
                    byte[] keyBytes = vEntry.getKey().getBytes();
                    builder.appendInt(keyBytes.length);
                    builder.appendBytes(keyBytes);
                    builder.appendInt(vEntry.getValue());
                }
            }
            return builder.getUnderlyingSlice();
        } catch (Exception e) {
            throw new RuntimeException(
                    e
                            + " ---- serialize err  requiredBytes = "
                            + requiredBytes
                            + " keyByteLen= "
                            + keyByteLen
                            + " keyListLen = "
                            + keyListLen);
        }
    }

    long estimatedInMemorySize() {
        return INSTANCE_SIZE + contentEstimatedSize;
    }

    public void mergeWith(CollectListStats other) {
        if (other == null) {
            return;
        }
        for (Map.Entry<Integer, Map<String, Integer>> cEntry : other.collectContainer.entrySet()) {
            Integer id = cEntry.getKey();
            Map<String, Integer> kMap = cEntry.getValue();
            for (Map.Entry<String, Integer> kEntry : kMap.entrySet()) {
                addCollectList(id, kEntry.getKey(), kEntry.getValue());
            }
        }
    }
}
