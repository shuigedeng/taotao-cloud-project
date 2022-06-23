
package com.taotao.cloud.sys.api.web.dto.mongo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 集合信息展示
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto {
    private String collectionName;
//    private CommandResult stats;
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

}
