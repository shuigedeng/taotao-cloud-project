
package com.taotao.cloud.sys.biz.tools.mongodb.dtos;

import com.mongodb.CommandResult;
import lombok.Data;

/**
 * 集合信息展示
 */
@Data
public class CollectionDto {
    private String collectionName;
    private CommandResult stats;
//    private int documents;
//    private int views;
//    private int objects;
//    private double avgObjSize;
//    private double dataSize;
//    private int storageSize;
//    private int numExtents;
//    private int indexes;
//    private double indexSize;
//    private double scaleFactor;
//    private double fsUsedSize;
//    private double fsTotalSize;

    public CollectionDto() {
    }

    public CollectionDto(String collectionName, CommandResult stats) {
        this.collectionName = collectionName;
        this.stats = stats;
    }
}
